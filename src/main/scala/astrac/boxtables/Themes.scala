package astrac.boxtables

object Themes {
  val simple = Theme[String](
    borders = Sides.hv(h = "|", v = "-"),
    corners = Corners.all("+"),
    dividers = Dividers.hv(h = "-", v = "|"),
    padding = Padding(space = Spacing.all(0), fill = Sides.all(" ")),
    margins = Margins(space = Spacing.all(0), fill = Sides.all("")),
    intersections = Intersections.hvc(h = "|", v = "-", c = "-")
  )

  val singleLineAscii = Theme[String](
    borders = Sides.hv(h = "│", v = "─"),
    corners = Corners(tl = "┌", tr = "┐", bl = "└", br = "┘"),
    dividers = Dividers.hv(v = "│", h = "─"),
    padding = Padding(space = Spacing.all(1), fill = Sides.all(" ")),
    margins = Margins(space = Spacing.all(1), fill = Sides.all(" ")),
    intersections = Intersections(l = "├", r = "┤", b = "┴", t = "┬", c = "┼")
    // B            ╡   ╢   ╖   ╕   ╣   ║   ╗   ╝   ╜   ╛
    //   C              ╞   ╟   ╚   ╔   ╩   ╦   ╠   ═   ╬   ╧
    //   D  ╨   ╤   ╥   ╙   ╘   ╒   ╓   ╫   ╪
  )

  val unicodeFrame = Theme[String](
    borders = Sides.hv(h = "┃", v = "━"),
    corners = Corners(tl = "╆", tr = "╅", bl = "╄", br = "╃"),
    dividers = Dividers.hv(v = "┃", h = "━"),
    padding = Padding(space = Spacing.all(1), fill = Sides.all(" ")),
    margins = Margins(space = Spacing.all(1), fill = Sides.all("░")),
    intersections = Intersections(l = "╊", r = "╉", b = "╇", t = "╈", c = "╋")
  )

  val blank = Theme[String](
    borders = Sides.all(" "),
    corners = Corners.all(" "),
    dividers = Dividers.hv(" ", " "),
    padding = Padding(space = Spacing.all(1), fill = Sides.all(" ")),
    margins = Margins(space = Spacing.all(1), fill = Sides.all(" ")),
    intersections = Intersections.all(" ")
  )

  val blankCompact = blank.copy(
    padding = blank.padding.copy(space = Spacing.zero),
    margins = blank.margins.copy(space = Spacing.zero),
  )

  val markdownHeader = Theme[String](
    borders = Sides(t = " ", r = "|", b = "-", l = "|"),
    corners = Corners(tl = " ", tr = " ", bl = "|", br = "|"),
    dividers = Dividers.hv(h = "-", v = "|"),
    padding = Padding(space = Spacing.hv(h = 1, v = 0), fill = Sides.all(" ")),
    margins = Margins(space = Spacing.all(0), fill = Sides.all(" ")),
    intersections = Intersections.hvc(h = "|", v = " ", c = "|")
  )

  val markdownMain = Theme[String](
    borders = Sides.hv(h = "|", v = " "),
    corners = Corners(tl = "|", tr = "|", bl = " ", br = " "),
    dividers = Dividers(v = Some("|"), h = None),
    padding = Padding(space = Spacing.hv(h = 1, v = 0), fill = Sides.all(" ")),
    margins = Margins(space = Spacing.all(0), fill = Sides.all(" ")),
    intersections = Intersections.hvc(h = "|", v = " ", c = "|")
  )
}
