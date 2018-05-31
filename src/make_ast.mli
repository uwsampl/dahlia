open Ast

val make_assignment : id -> expression -> command

val make_int : int -> expression

val make_bool : bool -> expression

val make_binop : binop -> expression -> expression -> expression

val make_for : id -> int -> int -> command list -> command

val make_var : id -> expression

val make_array : int -> type_annotation -> expression

val make_array_update : id -> expression -> expression -> command