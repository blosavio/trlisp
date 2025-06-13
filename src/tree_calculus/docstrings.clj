;; out-of-band docstrings for tree calculus defintions


(doseq [s ['length-is?
           'leaf?
           'stem?
           'fork?]]
  (register s :no-doc))


(register 'Δ
          "*Node operator*. Both a value and a function. By itself, equivalent
 to an empty Clojure vector. When at the head of an evaluated list, behaves as
 a function, invokes [[Apply]], the tree calculus application rules.

```
(Δ) ;; => Δ, or equivalently []

(Δ x) ;; returns a stem of x
(Δ x y) ;; returns a fork of x and y```")


(register 'K
          "*K combinator*. When supplied with two arguments, returns the
 first, discarding the second.

```(K x y) ;; => x```")


(register 'I
          "*I combinator*. When applied to a single argument, `x`, returns
exactly `x`, i.e., its _identity_.

```(I x) ;; => x```")


(register 'D
          "*D combinator*. When applied to three arguments, reverses order of
of first two arguments and broadcasts their application to the third argument.

```(D x y z) ;; => ((y z) (x z))```

See also [[d]].")


(register 'd
          "Shortcut function for *[[D]] combinator*. The D combinator applied
to the first, `x`, of its three arguments always reduces to `(Δ (Δ x))`.

```(D x y z) ;; => ((Δ (Δ x)) y z)```

is equivalent to

```((d x) y z)```

See page 29 of _[Reflective Programs in Tree Calculus](https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf)_.")


(register 'S
          "*S combinator*. When applied to three arguments, broadcasts the
third argument to each of the first two arguments.

```(S x y z) ;; => ((x z) (y z))```")


(register 'B
          "*B combinator*. When applied to three arguments, subverts the
 left-associative application ordering.

```(B x y z) ;; => (x (y z))```")


(register 'C
          "*C combinator*. Swaps position of second and third arguments.

```(C x y z) ;; => (x z y)```")


(register 'W
          "*W combinator*. Duplicates the second argument.

```(W x y) ;; => (x y y)```")


(register 'True
          "*Logical true*. Opposite of [[False]].

```(Not True) ;; => False```

In the operator position, returns the first of two arguments.")


(register 'False
          "*Logical false*. Opposite of [[True]].

```(Not False) => True```

In the operator position, returns the second of two arguments.")


(register 'And
          "*Logical conjunction*. Returns [[True]] if and only if both
operands are `True`.

```
(And True True) ;; => True
(And False True) ;; => False
```")


(register 'Or
          "*Logical disjunction*. Returns [[True]] if either of two operands
are `True`.

```
(Or True False) ;; => True
(Or False False) ;; => False
```")


(register 'Implies
          "*Material implication*. Returns [[True]] if and only if
first operand is `True` and the second operand is `False`.

```
(Implies True False) ;; => True
(Implies True True) ;; => False
(Implies True False) ;; => False
(Implies False False) ;; => False
```")


(register 'Not
          "*Logical negation*. Returns opposite Boolean.

```
(Not True) ;; => False
(Not False) ;; => True
```")


(register 'Iff "*Biconditional*. Returns [[True]] if both operands are `True` or
both operands are `False`.

```
(Iff True True) ;; => True
(Iff True False) ;; => False
(Iff False True) ;; => False
(Iff False False) ;; => True
```")


(register 'Zero?
          "Returns [[True]] if operand, a tree representation of a natural
 number, is zero, [[False]] otherwise.")


(register 'Pair
          "Returns a 2-tuple of `x` and `y`.

See also [[First]] and [[Second]].

```
(First (Pair x y)) ;; => x
(Second (Pair x y)) ;; => y
```")


(register 'First
          "Returns the first element of a [[Pair]].

```(First (Pair x y)) ;; => x```

See also [[Second]].")


(register 'Second
          "Returns the second element of a [[Pair]].

```(Second (Pair x y)) ;; => y```

See also [[First]].")


(register 'Successor
          "Returns the next natural number.

```(Successor n) ;; => (+ n 1)```

See also [[Predecessor]].")


(register 'Predecessor
          "Returns the previous natural number.

```(Predecessor n) ;; => (- n 1)```

See also [[Successor]].")


(register 'Query :no-doc)


(register 'Leaf?
          "Returns [[True]] when supplied with a leaf, otherwise [[False]]..

```(Leaf? Δ) ;; => True```

See also [[Stem?]] and [[Fork?]]")


(register 'Stem?
          "Returns [[True]] when supplied with a stem, otherwise [[False]]..

```(Stem? ΔΔ) ;; => True```

See also [[Leaf?]] and [[Fork?]].")


(register 'Fork?
          "Returns [[True]] when supplied with a fork, otherwise [[False]].

```(Fork? ΔΔΔ) ;; => True```

See also [[Leaf?]] and [[Stem?]].")


(register 'Wait :no-doc)
(register 'Wait-1 :no-doc)
(register 'Wait-2 :no-doc)


(register 'Self-Apply
          "Applies a single argument to itself.

```(Self-Apply x) ;; => (x x)```")


(register 'Z :no-doc)


(register 'Swap
          "Swap the argument order of 2-arity function `f`.

```((Swap f) x y) ;; => (f y x)```")


(register 'Y
          "*Y combinator*. Recursively apply a function to itself.

```(Y f) ;; => (f (Y f))```")


(register 'Plus
          "Add two natural numbers.

```(Plus x y) ;; => (+ x y)```")


(register 'Minus
          "Subtract two natural numbers.

```(Minus x y) ;; => (- x y)```")


(register 'Times
          "Multiply two natural numbers.

```(Times x y) ;; => (* x y)```")


(register 'Divide
          "Divide two natural numbers.

```(Divide x y) ;; => (/ x y)```")


(register 'T-Nil
          "Empty list.")


(register 'T-Cons
          "*Cons*truct a list by appending `h` to list `t`.

```(T-Cons h (List t1 t2 t3)) ;; => (h t1 t2 t3)```")


(register 'T-Head
          "Returns the head of a list.

```(T-Head (List x1 x2 x3)) ;; => x1```")


(register 'T-Tail
          "Returns the tail of a list.

```(T-Tail (List x1 x2 x3)) ;; => (List x2 x3)```")


(register 'List-Map-Swap :no-doc)


(register 'Map
          "Apply a function to each element of a list.

```(Map Successor (List x y z)) ;; => (List (Successor x) (Successor y) (Successor z))```")


(register 'List-FoldLeftAux :no-doc)


(register 'Fold-Left
          "Apply a function `f` to an initial value `x` and the first element of
 a list `y`, then apply the function to that result and next element of the list, etc.

```(Fold-Left Divide sixty (List three four)) ;; => five```")


(register 'List-FoldRightAux :no-doc)


(register 'Fold-Right
          "Apply function `f` to an initial value `x` and a value obtained by
applying `f` to the first element of list `y` and the value obtained by applying
`f` to the second element, etc.

```(Fold-Right Divide one (List sixty one-hundred-twenty four)) ;; => two```")


(register 'Append
          "Append list `xs` to the head of list `ys`.

```(Append (List x y z)  (List a b c)) ;; => (List x y z a b c)```")


(register 'Reverse
          "Reverses list `z`.

```(Reverse (List a b c)) ;; => (List c b a)```")


(register 'Size
          "Returns the number of nodes in the argument.

```(Size (Δ Δ Δ)) ;; => three```")


(register 'Equal?
          "Returns [[True]] if the two arguments are equal.

```(Equal? (Δ Δ) (Δ Δ Δ)) ;; => False```")


(register 'Tag
          "Apply a tag `t` to form `f`. The tagged `f` applied to some other
 argument `x` is indistinguishable from untagged `f` applied to the same
 argument `x`.

```((Tag t f) x) ;; => (f x)```

See also [[Get-Tag]] and [[Un-Tag]].")


(register 'Get-Tag
          "Returns the tag of a tagged form.

```(Get-Tag (Tag t f)) ;; => t```

See also [[Tag]] and [[Get-Tag]].")


(register 'Un-Tag
          "Recover `f` after tagging.

```(Un-Tag (Tag t f)) ;; => f```

See also [[Tag]] and [[Get-Tag]].")


(register 'Tag-Wait :no-doc)


(register 'Y-t
          "*Tagged Y combinator*. Apply tag `t` to fixpoint function `f`.

```(Y-t t f) ;; => (f (Y-t t f))```

Tags can not be added after the fixpoint has been formed, so the usual [[Y]]
combinator can not be used.")


(register 'error
          "Value returned when [[Type-Check]] determines that two tags fail to
conform. See also [[Typed-App]].")


(register 'Type-Check
          "Compare tags of `x` and `u`. Returns [[error]] if tags do not
 conform. See also [[Typed-App]].")


(register 'Typed-App
          "*Typed application*. Apply tagged function `f` to tagged argument
 `x`, returning a tagged value.

```(Typed-App (Tag True Successor) (Tag True n)) ;; => (Tag true (+ n 1))```")


(register 'Stem?-2
          "Returns `Δ` if argument is a leaf or a fork, or returns `(Δ (K (K Δ)) (x (K (K Δ))))` for a stem of form `(Δ x)`. Used by [[Triage]].")


(register 'Fork?-2
          "When argument is a:

* leaf, returns `(K K)`
* stem of form `(Δ x)`, returns `(K (Δ x (K (K Δ))))`
* fork, returns `Δ`.

Used by [[Triage]].")


(register 'Triage
          "Applies a specified function, `f`*n*, depending on whether argument
 is a leaf, stem, or fork.

If argument is a:

* leaf `Δ`, returns `(f0)`
* stem `(Δ x)`, returns `(f1 x)`
* fork `(Δ x y)`, returns `(f2 x y)`")


(register 'Size-Variant
          "Returns the number of nodes in argument. Variant of [[Size]]
 implemented with [[Triage]].

```(Size-Variant (Δ Δ Δ)) ;; => three```")


(register 'Equal?-Variant
          "Returns [[True]] if two arguments are equal. Variant of [[Equal?]]
implemented with [[Triage]].

```(Equal?-Variant (Δ Δ) (Δ Δ Δ)) ;; => False```")


(doseq [s ['Leaf-Case
           'Stem-Case
           'Fork-Case-Aux-1
           'Fork-Case-Aux-2
           'Fork-Case
           'Tree-Case
           'Tree-Case-2]]
  (register s :no-doc))


(register 'Extension
          "*Pattern matching*. Returns a function accepting a single argument
 `x`, that when applied to `x` returns various patterns below. `Extension`
 requires a pattern `p`, body `s`, and a 'always-applied' functions `r`.

Use when you have a pattern `(Δ :a :b)` and a test `(Δ :c :d)`, and want to ask
*What is the thing in `test` at the same location as `:a` in `pattern`?* Answer:
`:c`.

See [unit tests](https://github.com/blosavio/trlisp/blob/34755e29b217fe8847483921f46b1366f7106df7/test/tree_calculus/definitions_test.clj#L591-L660)
 for example usage.")


(register 'Extension-2
          "Variant of [[Extension]] that uses native tree calculus predicates,
i.e., does not rely on Clojure host's `cond`itional form.")


(doseq [s ['Eager
           'Db
           'BF-Leaf
           'BF-Stem
           'BF-Fork
           'On-Fork]]
  (register s :no-doc))


(register 'BF
          "*Branch-First self-evaluator*. Apply `x` to `y` using a strategy that
evaluates all branches before evaluating the root.

```(BF Not False) ;; => True```")


(doseq [s ['Quote-Aux
           'Root-L
           'Root-S
           'Root-F
           'Root-N
           'Root-Aux]]
  (register s :no-doc))


(register 'Quote
          "*Quotation*. Halts immediate application. Recursively descends
 through a tree and pushes all nodes to leaves of the quoted form. Quoted trees
are explicitly consumed by [[Root]] and [[RB]], and implicitly by [[RF]].

```(Quote (Δ Δ Δ)) ;; => (Δ (Δ Δ Δ) Δ)```")


(register 'Root
          "*Root self-evaluator*. Apply `m` to `n`, doing only enough
 computation to determine the nature of the root. Requires [[Quote]]-ing the
 arguments.

Relationship between the input and output are not straightforward. E.g.,

```(Root (Δ (Quote (Δ y)) (Quote z))) ;; => (Δ (Quote y) (Quote z))```

See [[RB]] and [[RF]] for more ergonomic evaluators.")


(register 'RB-Aux :no-doc)


(register 'RB
          "*Root-Branch self-evaluator*. Performs root evaluation then
 recursively evaluates the branches to produce a normal form. Consumes a single
 tree, a fork whose branches are each [[Quote]]d.

```(RB (Δ (Quote Not) (Quote False))) ;; => True```")


(register 'RF
          "*Root-First self-evaluator*. Applies `f` to `z`, quotation implicit.

```(RF Not False) ;; => True```")


(register 'ΔΔ
          "Stem resulting from evaluating `(Δ Δ)`.")


(register 'ΔΔΔ
          "Fork resulting from evaluating `(Δ Δ Δ)`.")


(register 'Anything-to-Not :no-doc)


(register 'bit->Bit
          "Given a Clojure/Java bit, returns a tree calculus Bit.

See also [[Bit->bit]].")


(register 'Bit->bit
          "Given a tree calculus Bit, returns a Clojure/Java bit.

See also [[bit->Bit]].")

