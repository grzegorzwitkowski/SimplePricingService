Given price list are defined this way:
--0: BOLD: 0.70, HIGHLIGHT: 1.00, PHOTO: 0.50
  |
  |--1: BOLD: 0.60, PHOTO: 0.40
  |  |
  |  |--2: BOLD: 0.50
  |  |
  |  |--3: BOLD: 0.40, PHOTO: 0.30
  |
  |--4: PHOTO: 0.40
When creating offer in category 3 with promo options BOLD,PHOTO,HIGHLIGHT
Then price should equal 1.70
