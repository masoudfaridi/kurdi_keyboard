#!/usr/bin/env python3
"""Builds the guide screenshots (app/src/main/res/drawable-nodpi/guide_XX.webp).

Input: raw 1080x2400 emulator screenshots s01.png ... s12.png (and s04b, s09a/b, s10a/b)
in the folder given as the first argument. Each picture is cropped to the part that
matters and marked: a finger where to touch, arrows for slides, a frame around the spot.

Usage: python tools/make_guide_images.py path/to/screenshots
"""
import sys
from pathlib import Path
from PIL import Image, ImageDraw

SRC = Path(sys.argv[1])
OUT = Path(__file__).resolve().parents[1] / "app/src/main/res/drawable-nodpi"
OUT.mkdir(parents=True, exist_ok=True)

ACCENT = (255, 122, 0, 255)      # frames and arrows (orange stands out on every theme)
FINGER = (20, 30, 45, 120)


def load(name):
    return Image.open(SRC / name).convert("RGBA")


def frame(im, box, r=22, w=8):
    d = ImageDraw.Draw(im)
    d.rounded_rectangle(box, radius=r, outline=ACCENT, width=w)


def finger(im, x, y, r=44):
    layer = Image.new("RGBA", im.size, (0, 0, 0, 0))
    d = ImageDraw.Draw(layer)
    d.ellipse((x - r, y - r, x + r, y + r), fill=FINGER, outline=(255, 255, 255, 230), width=5)
    im.alpha_composite(layer)


def arrow(im, x0, y0, x1, y1, w=12, head=34, both=False):
    import math
    d = ImageDraw.Draw(im)
    d.line((x0, y0, x1, y1), fill=ACCENT, width=w)
    def tip(xa, ya, xb, yb):
        a = math.atan2(yb - ya, xb - xa)
        p1 = (xb - head * math.cos(a - 0.5), yb - head * math.sin(a - 0.5))
        p2 = (xb - head * math.cos(a + 0.5), yb - head * math.sin(a + 0.5))
        d.polygon([(xb, yb), p1, p2], fill=ACCENT)
    tip(x0, y0, x1, y1)
    if both:
        tip(x1, y1, x0, y0)


def crop(im, top, bottom):
    return im.crop((0, top, im.width, bottom))


def side_by_side(a, b, gap=24):
    h = max(a.height, b.height)
    out = Image.new("RGBA", (a.width + b.width + gap, h), (244, 246, 248, 255))
    out.paste(a, (0, 0))
    out.paste(b, (a.width + gap, 0))
    return out


def stacked(a, b, gap=24):
    w = max(a.width, b.width)
    out = Image.new("RGBA", (w, a.height + b.height + gap), (244, 246, 248, 255))
    out.paste(a, ((w - a.width) // 2, 0))
    bx, by = (w - b.width) // 2, a.height + gap
    out.paste(b, (bx, by))
    ImageDraw.Draw(out).rounded_rectangle((bx - 2, by - 2, bx + b.width + 1, by + b.height + 1),
                                          radius=10, outline=(200, 206, 214, 255), width=3)
    return out


def save(im, n, width=720):
    im = im.convert("RGB")
    if im.width != width:
        im = im.resize((width, round(im.height * width / im.width)), Image.LANCZOS)
    path = OUT / ("guide_%02d.webp" % n)
    im.save(path, "WEBP", quality=80, method=6)
    print(path.name, im.size, path.stat().st_size // 1024, "KB")


# 1 start screen: setup + languages
im = load("s01.png")
frame(im, (42, 372, 1038, 845))
frame(im, (42, 890, 1038, 1405))
save(crop(im, 120, 1420), 1)

# 2 language list: hold space, slide up
im = load("s02.png")
finger(im, 540, 2184)
arrow(im, 600, 2120, 600, 1840)
save(crop(im, 1320, 2275), 2)

# 3 suggestions
im = load("s03.png")
frame(im, (6, 1335, 1074, 1445))
save(crop(im, 1180, 2275), 3)

# 4 swipe: the trail + finger at the end
im = load("s04.png")
finger(im, 324, 1848)
save(crop(im, 1460, 2275), 4)

# 5 cursor: slide on the space bar
im = load("s04b.png")
finger(im, 540, 2184)
arrow(im, 380, 2184, 700, 2184, both=True)
frame(im, (1, 1180, 1079, 1310), r=18, w=6)
save(crop(im, 1180, 2275), 5)

# 6 hold a key, slide to an option
im = load("s06.png")
finger(im, 694, 1992)
save(crop(im, 1460, 2275), 6)

# 7 number pad
im = load("s07.png")
frame(im, (6, 2112, 124, 2266), r=18)
save(crop(im, 1180, 2275), 7)

# 8 tools row
im = load("s08.png")
frame(im, (6, 1338, 1074, 1440))
save(crop(im, 1180, 2275), 8)

# 9 ready sentences: list + send options
a = load("s09a.png")
frame(a, (34, 470, 1046, 1020))
a = crop(a, 270, 1040)
b = load("s09b.png").crop((72, 924, 1008, 1476))
save(stacked(a, b), 9)

# 10 contacts: list + what to send
a = crop(load("s10a.png"), 270, 1200)
b = load("s10b.png").crop((72, 862, 1008, 1538))
save(stacked(a, b), 10)

# 11 emoji panel
im = load("s11.png")
save(crop(im, 1330, 2275), 11)

# 12 themes
im = load("s12.png")
save(crop(im, 290, 1640), 12)
