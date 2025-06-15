[:section#comments
 [:h2 "Comments"]

 [:p "Two of tree calculus' ideas caught my attention."]

 [:ol
  [:li
   [:strong "One operator plus six application rules"]

   [:p "Tree calculus' minimalism hints that it could be implemented in
 constrained environments. Almost certainly a 6502 microprocessor, probably on a
 punch card machine, but maybe even a marble machine by someone really clever."]

   [:p "Tree calculus is so compact, we can evaluate the expressions by hand. I
 wrote trlisp so that I could evaluate tree calculus expressions to check my
 pencil and paper work, and to feel what it's like to program with. In that
 regard, trlisp provides that. While working through the book's examples, trlisp
 can quickly validate an expression that takes multiple sheets of paper."]

   [:p "On the other hand, it is immediately apparent that trlisp is in no way a
 practical, general-purpose programming language. The inputs and return values
 are trees, not plainly understandable integers and strings, and bugs are
 tedious to track down. It's not clear to me that there is any advantage to
 writing an entirely new programming language (and associated tooling) based on
 tree calculus, or grafting tree calculus' evaluation model onto an existing
 language."]

   [:p "However, adapting some of tree calculus' "
    [:em "features"]
    " to an existing programming language could be beneficial."]]

  [:li
   [:strong "Every entity composed of the same stuff"]

   [:p "Check this out."]

   [:pre (replace-trees (print-form-then-eval "(Second D)"))]

   [:p "We just reached into the D combinator and pulled out its second element,
 i.e., the right child. We didn't resort to quoting or a macro. The tree that
 implements the D combinator when it's at the head of a list, is, at any moment,
 available for inspection. There is no separate notion of a function's
 definition that is distinct from the thing that executes the task."]

   [:p "Having such reflective functions, direct access to a function's
 definition, suggests some interesting possibilities. Being able to inspect,
 analyze, optimize, modify, and borrow pieces from a function could enable tasks
 that are not currently easy, or even possible. In recent memory, I have at
 least "
    [:a {:href "https://blosavio.github.io/speculoos/speculoos.utility.html#var-defpred"}
     "one utility"]
    " that would have been faster and more elegant to write if I had run-time
 (not compile-time) access to the function's definition."]

   [:p "Could it be generally useful to be able to define a new function by
 modifying an existing one?"]

   [:pre
    [:code ";; pseudo-code"]
    [:br]
    [:code "(def my-new-fn (assoc old-fn ...))"]]

   [:p "When we speak of 'first-class functions', we generally mean that
 functions may be passed as values and returned from other functions. But what
 if we could dive into a function and do something useful with its contents?
 Wouldn't reflective functions truly be 'first-class'?"]

   [:p "We probably wouldn't use it often, but it could be nice tool in the
 toolbox. Something akin to writing a Lisp macro when a regular function won't
 suffice. I can more easily imagine how this particular tree calculus feature
 might be added to an existing language."]]]
 ]

