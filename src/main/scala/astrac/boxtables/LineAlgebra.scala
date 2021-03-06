package astrac.boxtables

import cats.kernel.Monoid
import cats.instances.list._
import cats.syntax.apply._
import cats.syntax.monoid._
import cats.syntax.foldable._
import cats.syntax.traverse._
import Sizing._

trait LineAlgebra[T, R] {
  implicit def T: Monoid[T]
  implicit def F: Formatter[T]
  implicit def R: Row[R]

  val sizing: Rows[T, Sizing] = Rows.sizing
  val theme: Rows[T, Theme[T]] = Rows.theme

  def transpose(ls: List[List[T]]): Rows[T, List[List[T]]] = {
    val w = ls.map(_.size).max
    ls.zipWithIndex
      .traverse {
        case (l, i) =>
          cellWidth(i).map(cw => l.padTo(w, T.combineN(F.space, cw)))
      }
      .map(_.transpose)
  }

  def boundedSpace(w: Int): Rows[T, Int] = theme.map { t =>
    math.max(0,
             w - 2 -
               (R.size - 1) -
               (t.padding.space.l * R.size) -
               (t.padding.space.r * R.size) -
               t.margins.space.l -
               t.margins.space.r)
  }

  val cellSpace: Rows[T, Int] = sizing.flatMap {
    case Equal(t)       => boundedSpace(t)
    case Fixed(cs)      => Rows.pure(cs.sum)
    case Weighted(t, _) => boundedSpace(t)
  }

  def cellWidth(idx: Int): Rows[T, Int] =
    (sizing, cellSpace).mapN[Int] { (sizing, cellSpace) =>
      val base = sizing match {
        case Equal(_)            => cellSpace / R.size
        case Fixed(cs)           => cs(idx)
        case w @ Weighted(_, ws) => cellSpace / w.sum * ws(idx)
      }

      val rounding = sizing match {
        case Equal(_) if idx < cellSpace % R.size          => 1
        case w @ Weighted(_, _) if idx < cellSpace % w.sum => 1
        case _                                             => 0
      }

      base + rounding
    }

  def cell(paddingL: T, paddingR: T, rowsDivider: T)(content: T,
                                                     index: Int): Rows[T, T] =
    theme.map { t =>
      val body = T.combineN(paddingL, t.padding.space.l) |+| content
      val pr = T.combineN(paddingR, t.padding.space.r)

      if (index == R.size - 1) body |+| pr
      else body |+| pr |+| rowsDivider
    }

  def line(marginL: T,
           borderL: T,
           paddingL: T,
           cells: List[T],
           rowsDivider: T,
           paddingR: T,
           borderR: T,
           marginR: T): Rows[T, T] = theme.flatMap { t =>
    cells.zipWithIndex
      .foldMap((cell(paddingL, paddingR, rowsDivider) _).tupled)
      .map(
        body =>
          T.combineN(marginL, t.margins.space.l) |+|
            borderL |+|
            body |+|
            borderR |+|
            T.combineN(marginR, t.margins.space.r))
  }

  def fillCells(f: T): Rows[T, List[T]] =
    (0 until R.size).toList
      .traverse(idx => cellWidth(idx).map(cw => T.combineN(f, cw)))

  def marginLine(f: T): Rows[T, T] =
    fillCells(f).flatMap(line(f, f, f, _, f, f, f, f))

  def borderLine(border: T,
                 intersectL: T,
                 intersectM: T,
                 intersectR: T): Rows[T, T] =
    (theme, fillCells(border))
      .mapN { (t, cs) =>
        line(t.margins.fill.l,
             intersectL,
             border,
             cs,
             intersectM,
             border,
             intersectR,
             t.margins.fill.r)
      }
      .flatMap(identity)

  def contentLine(contents: List[T]): Rows[T, T] =
    theme.flatMap { t =>
      line(t.margins.fill.l,
           t.borders.l,
           t.padding.fill.l,
           contents,
           t.dividers.v.getOrElse(F.space),
           t.padding.fill.r,
           t.borders.r,
           t.margins.fill.r)
    }

  def paddingLine(p: T): Rows[T, T] =
    (theme, fillCells(p))
      .mapN { (t, cs) =>
        line(t.margins.fill.l,
             t.borders.l,
             p,
             cs,
             t.dividers.v.getOrElse(F.space),
             p,
             t.borders.r,
             t.margins.fill.r)
      }
      .flatMap(identity)
}
