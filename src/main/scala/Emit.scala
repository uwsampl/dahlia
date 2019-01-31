package fuselang

import org.bitbucket.inkytonik.kiama.output._

object Emit extends PrettyPrinter {

  import scala.language.implicitConversions
  import Syntax._
  import TypeEnv.Env

  override val defaultIndent = 2

  private def scope(doc: Doc): Doc =
    lbrace <@> indent(doc) <@> rbrace

  private implicit def op2ToDoc(op: Op2): Doc = op match {
    case OpEq => "="
    case OpAdd => "+"
  }

  private implicit def typeToDoc(typ: Type): Doc = typ match {
    case TBool | TIndex(_, _) | TStaticInt(_) => "int"
    case TSizedInt(_) => value(typ)
    case TArray(typ, dims) => typ <> brackets(value(dims.map(_._1).foldLeft(1)(_ * _)))
  }

  private implicit def exprToDoc(e: Expr): Doc = e match {
    case EInt(i) => value (i)
    case EBool(b) => value(if(b) 1 else 0)
    case EVar(id) => value(id)
    case EBinop(op, e1, e2) => e1 <+> op <+> e2
    case EAA(id, idxs) => id <> hcat(idxs.map(idx => brackets(idx)))
  }

  private implicit def cmdToDoc(c: Command)(implicit env: Env): Doc = c match {
    case CDecl(id, typ) => typ <+> id <> semi
    case CSeq(c1, c2) => c1 <> semi <> line <> c2 <> semi
    case CLet(id, e) => env(id).typ <+> value(id) <+> equal <+> e <> semi
    case CIf(cond, cons) => "if" <> parens(cond) <> scope (cons)
    case CFor(iter, range, par, CReducer(reduce)) =>
      "for" <> parens {
        env(iter).typ <+> iter <+> "=" <+> value(range.s) <> semi <+>
        iter <+> "<" <+> value(range.e) <> semi <+>
        iter <+> "++"
      } <+> scope(par <> line <> text("// reducer:") <> reduce)
    case CUpdate(lhs, rhs) => lhs <+> "=" <+> rhs <> semi
    case CExpr(e) => e <> semi
    case CEmpty => ""
    case CRefreshBanks => "//---"
  }

  def emitC(c: Command, env: Env) =
    super.pretty(cmdToDoc(c)(env)).layout

}
