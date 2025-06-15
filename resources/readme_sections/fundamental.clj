[:section#fundamental
 [:h2 "Fundamental combinators"]

 [:p "Tree calculus functions and values could be defined purely in terms of "
  [:code "Δ"]
  ", but some judicious naming helps make programming in trlisp more ergonomic.
 Let's "
  [:a {:href "https://blosavio.github.io/trlisp/tree-calculus.definitions.html"}
   "define"]
  " a small number of fundamental functions, and along the way, we will see the
 application rules in action."]

 [:h3#K-combinator "K combinator"]

 [:p "We'll start with the simplest function, the "
  [:em "K combinator"]
  ": simple in what it does, simple in structure, and simple in its
 application."]

 [:p "The K combinator always returns the first of its two arguments."]

 [:pre (replace-trees (print-form-then-eval "(K Δ (Δ Δ Δ))"))]

 [:p "Given trees "
  [:code "Δ"]
  " and "
  [:code "(Δ Δ Δ)"]
  ", "
  [:code "K"]
  " returns "
  [:code "Δ"]
  ", the first argument, and discards the second argument. Here, we are using
 concrete trees to demonstrate the behavior, but "
  [:code "K"]
  " works the same on any pair of trees."]

 [:p "We've already seen in our discussion of the tree calculus application
 rules that "
  [:a {:href "#apply-fork-leaf"} "Rule 3"]
  " declares that a fork-leaf always returns the first of a pair of arguments.
 So we know that we can create a K combinator by adopting that fork-leaf
 pattern." ]

 [:p "Putting an expression that evaluates to a fork-leaf "
  [:code "(Δ Δ)"]
  " in the function position and applying it to two arguments, dispatches on
 application Rule 3, and returns the first argument."]

 [:pre (replace-trees (print-form-then-eval "((Δ Δ) Δ (Δ Δ Δ))"))]

 [:p
  [:code "(Δ Δ)"]
  " evaluates to "
  [:code "K"]
  ", so this expression returns the exact same "
  [:code "Δ"]
  " as before."]

 [:p "We've demonstrated that once we've composed a tree that triggers a
 particular tree calculus application rule, we can use that tree to get a
 wanted behavior."]

 [:p "In fact, trlisp's "
  [:a
   {:href "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L102"}
   "actual definition"]
  " shows us that "
  [:code "K"]
  " is simply defined as "
  [:code "(Δ Δ)"]
  ". Now that the symbol "
  [:code "K"]
  " is bound to a tree that creates a fork-leaf tree when given two arguments, we
 can easily re-use the semantic idea of "
  [:em "Take the first argument, discard the second argument"]
  " in any future expression."]

 [:h3#D-combinator "D combinator"]

 [:p "A step up in complexity is the "
  [:em "D combinator"]
  ". Applied to three arguments, it does this."]

 [:pre [:code "(D x y z) = ((y z) (x z))"]]

 [:p "This operation is useful when we want to replicate an element and/or when
 we want to swap the order of two items."]

 [:p "We define D like so."]

 [:pre [:code "(Def D (Δ (Δ Δ) (Δ Δ Δ)))"]]

 [:p "We can visualize D like this."]

 [:pre (make-fork-stem "Δ" "(K Δ)")]

 [:p "To see D in action, we'll assign "
  [:code "x"]
  ", "
  [:code "y"]
  ", and "
  [:code "z"]
  " to concrete trees, any of which may occupy the function position."]

 [:pre
  (print-form-then-eval "(def x (K Δ))") [:br]
  (print-form-then-eval "(def y (K Δ))") [:br]
  (print-form-then-eval "(def z K)")]

 [:p "These are merely small trees with no particular semantics. We use them
 only because it's straightforward to apply them by hand."]

 [:p "Let's evaluate the sub-components."]

 [:pre (replace-trees (print-form-then-eval "(y z)"))]

 [:p "because "
  [:code "(K Δ K)"]
  " yields "
  [:code "Δ"]
  " due to application of the K combinator."]

 [:p "The second sub-component is identical to the first."]

 [:pre (replace-trees (print-form-then-eval "(x z)"))]

 [:p "Now let's evaluate the value of "
  [:code "(y z)"]
  " applied to the value of "
  [:code "(x z)"]
  "."]

 [:pre (replace-trees (print-form-then-eval "(Δ Δ)"))]

 [:p "Let's check that against evaluating the entire expression."]

 [:pre (replace-trees (print-form-then-eval "((y z) (x z))"))]

 [:p "Finally, let's invoke trlisp's definition of "
  [:code "D"]
  " with "
  [:code "x"]
  ", "
  [:code "y"]
  ", and "
  [:code "z"]
  " as arguments."]

 [:pre (replace-trees (print-form-then-eval "(D x y z)"))]

 [:p "Notice that, as with the K combinator, the D combinator is a tree
 deliberately composed to invoke a particular application rule, in this
 instance, Rule 4 for fork-stems. After a couple rounds of application, we
 obtain the following picture."]

 [:pre [:code (make-fork-stem "x" "y" "z")]]

 [:p "Rule 4 immediately declares this to evaluate to"]

 [:pre [:code "((y z) (x z))"]]

 [:p "which is the D combinator's essence."]

 [:p "If we work out that sequence of evaluations by hand, we notice that D
 applied to the first argument "
  [:code "x"]
  " always eventually evaluates to"]

 [:pre [:code "(Δ (Δ x))"]]

 [:p "which looks like this."]

 [:pre [:code "Δ\n|\nΔ\n|\nx"]]

 [:p "It's convenient to short-cut those steps, so we designate a function "
  [:code "d"]
  " such that "]

 [:pre [:code "((d x) y z) ;; => ((y z) (x z))"]]

 [:p " which is the same result produced by the D combinator."]

 [:h3#S-combinator "S combinator"]

 [:p "The D combinator, in addition to broadcasting the third argument, also
 swaps the order of the first two arguments. It just so happens that there is
 a combinator that broadcasts the third argument to the first two arguments
 while maintaining the order."]

 [:p "The "
  [:em "S combinator"]
  " works like this"]

 [:pre [:code "(S x y z) ;; => ((x z) (y z))"]]

 [:p "and can be "
  [:a {:href "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L116-L117"}
   "defined"]
  " in terms of "
  [:code "d"]
  ", "
  [:code "D"]
  ", and "
  [:code "K"]
  "."]

 [:p "We can test the definition by evaluating with arguments "
  [:code "x"]
  ", "
  [:code "y"]
  ", and "
  [:code "z"]
  " from the previous subsection."]

 [:pre (print-form-then-eval "(= (S x y z) ((x z) (y z)))" 18 12)]

 [:p "The definition for S checks out."]

 [:p "It may be a little surprising to learn that the S combinator plays almost
 no role in any upcoming definition. It's main utility is that with only "
  [:code "S"]
  " and "
  [:code "K"]
  " in hand, we know that tree calculus is "
  [:em "S‑K complete"]
  ", and therefore can compute any function whatsoever."]

 [:h3#I-combinator "I combinator"]

 [:p "Another crucial combinator is the "
  [:em "I combinator"]
  ", which returns exactly its single argument."]

 [:pre [:code "(I x) ;; => x"]]

 [:p "The I combinator could be defined in terms of "
  [:code "S"]
  " and "
  [:code "K"]
  ", but such a definition is extensionally equivalent to trlisp's simpler "
  [:a {:href "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L105-L106"}
   "definition"]
  ","]

 [:pre [:code "(Δ K K)"]]

 [:p "which we can visualize like this."]

 [:pre [:code (str "  Δ\n"
                   " / \\\n"
                   "Δ   Δ\n"
                   "|   |\n"
                   "Δ   Δ")]]

 [:p "Note that the tree structure of I combinator is designed to invoke
 Rule 4, applying fork-stems, then immediately thereafter applies
 Rule 3, for fork-leaves to discard the second expression, returning
 exactly the argument. "
  [:strong "The structure encodes the behavior."]]

 [:p "The I combinator appears in many upcoming definitions, including a key
 role in defining Boolean entities."]

 [:h3#Y-combinator "Y combinator"]

 [:p "trlisp supports recursion via the fixpoint combinator, or "
  [:em "Y combinator"]
  ", used in many of the upcoming definitions. Y is awkward to use directly,
 but know that it exists and is available."]

 [:h3 "Other combinators"]

 [:p
  "For completeness, trlisp also provides the "

  [:a
   {:href "https://blosavio.github.io/trlisp/tree-calculus.definitions.html#var-B"}
   "B"]

  ", "

  [:a
   {:href "https://blosavio.github.io/trlisp/tree-calculus.definitions.html#var-C"} "C"]

  ", and "

  [:a
   {:href "https://blosavio.github.io/trlisp/tree-calculus.definitions.html#var-W"} "W"]

  " combinators, but does not use them for any further purposes."]

 [:p "It turns out that we wouldn't often directly use any of these combinators
 in day-to-day programming, but they are critical building blocks for the "
  [:a {:href "#base"} "functions"]
  " we "
  [:em "do"]
  " use."]
 ]
