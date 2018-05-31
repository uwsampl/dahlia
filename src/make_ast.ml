open Ast

let make_assignment id expr =
  CAssignment (id, expr)

let make_int x =
  EInt (x)

let make_bool b =
  EBool (b)

let make_binop bop e1 e2 =
  EBinop (bop, e1, e2)

let make_for x x1 x2 e =
  CFor (x, x1, x2, e)

let make_var id =
  EVar id

let make_array s t =
  match t with
  | AInt -> EArray (Array.make s (EInt 0))
  | ABool -> EArray (Array.make s (EBool false))

let make_array_update id i e =
  CArrayUpdate (id, i, e)