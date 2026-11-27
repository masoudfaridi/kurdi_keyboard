#!/usr/bin/env python3
"""Builds app/src/main/assets/dict/{en,fa,ckb}.dict from the kafe_keyboard word lists.

Output format (UTF-8, one entry per line, most frequent first):
    word<TAB>freq        freq is 1..255 on a log scale

Sources (see assets/dict/NOTICE.txt for licences):
    en : kafe_keyboard/src/main/assets/dictionaries/en_enhanced.json
    fa : kafe_keyboard/scripts/dictionaries/fa/fa_words.txt
    ckb: ~/.cache/kafe-corpora/ckb/build/ckb_words.txt (or langpack build output)

Usage: python tools/build_dicts.py [path-to-kafe_keyboard] [path-to-ckb_words.txt]
"""
import json, math, re, sys
from pathlib import Path

KAFE = Path(sys.argv[1] if len(sys.argv) > 1 else r"D:/keyboard/kafe_keyboard")
CKB = Path(sys.argv[2] if len(sys.argv) > 2 else Path.home() / ".cache/kafe-corpora/ckb/build/ckb_words.txt")
OUT = Path(__file__).resolve().parents[1] / "app/src/main/assets/dict"
LIMITS = {"en": 60000, "fa": 50000, "ckb": 40000}

def read_tsv(p):
    out = {}
    for line in Path(p).read_text(encoding="utf-8").splitlines():
        if not line or line.startswith("#"):
            continue
        parts = line.split("\t")
        if len(parts) < 2:
            continue
        try:
            out[parts[0].strip()] = float(parts[1])
        except ValueError:
            pass
    return out

def scale(d, lang, word_ok):
    items = [(w, f) for w, f in d.items() if f > 0 and word_ok(w)]
    items.sort(key=lambda x: -x[1])
    items = items[: LIMITS[lang]]
    hi = math.log(items[0][1] + 1); lo = math.log(items[-1][1] + 1)
    rows = []
    for w, f in items:
        v = 1 + int(round(254 * (math.log(f + 1) - lo) / max(hi - lo, 1e-9)))
        rows.append((w, max(1, min(255, v))))
    return rows

def write(lang, rows):
    OUT.mkdir(parents=True, exist_ok=True)
    with open(OUT / f"{lang}.dict", "w", encoding="utf-8", newline="\n") as f:
        for w, v in rows:
            f.write(f"{w}\t{v}\n")
    print(lang, len(rows), "words")

EN_OK = re.compile(r"^[a-z][a-z']*$").match
# en_enhanced.json values are already log-ish 0..255; recover a monotone pseudo-count.
en = json.loads((KAFE / "src/main/assets/dictionaries/en_enhanced.json").read_text(encoding="utf-8"))
en = {w: math.exp(v / 12.0) for w, v in en.items()}
write("en", scale(en, "en", lambda w: bool(EN_OK(w)) and (len(w) > 1 or w in ("a", "i"))))

FA_LETTERS = "آابپتثجچحخدذرزژسشصضطظعغفقکگلمنوهیئءأؤإة\u200c"
FA_OK = re.compile(f"^[{FA_LETTERS}]+$").match
FOLD = str.maketrans({"ي": "ی", "ى": "ی", "ك": "ک", "ـ": None})
fa = {}
for w, f in read_tsv(KAFE / "scripts/dictionaries/fa/fa_words.txt").items():
    w = w.translate(FOLD)
    fa[w] = fa.get(w, 0) + f
write("fa", scale(fa, "fa", lambda w: bool(FA_OK(w)) and (len(w) > 1 or w == "و")))

CKB_LETTERS = "ئابپتجچحخدرڕزژسشعغفڤقکگلڵمنهەوۆیێءھ\u200c"
CKB_OK = re.compile(f"^[{CKB_LETTERS}]+$").match
ckb = {}
for w, f in read_tsv(CKB).items():
    w = w.translate(FOLD)
    ckb[w] = ckb.get(w, 0) + f
write("ckb", scale(ckb, "ckb", lambda w: bool(CKB_OK(w)) and (len(w) > 1 or w == "و")))
