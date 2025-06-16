[:section#intro
 [:h2 "Introduction"]

 [:p "Barry Jay's unlabelled binary "
  [:a {:href "https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf"}
   "tree calculus"]
  " is "
  [:em "reflective"]
  ", which means we can inspect the internal structure of every
 function. Without quotation, every function is always open to:"

  [:ul
   [:li [:strong "Inspection"] " We may view the function's structure and
 contents."]

   [:li [:strong "Analysis"] " We may measure the function's size, etc."]

   [:li [:strong "Comparison"] " We may test two functions for equality,
 equivalence, etc."]

   [:li [:strong "Optimization"] " We may precompute some static branch of the
 function, or alter it for more efficiency."]

   [:li [:strong "Decomposition"] " We may pull out different pieces of the
 function, perhaps for use in a different context."]

   [:li [:strong "Modification"] " We may change some facet of the function at
 runtime to suit our needs."]]]

 [:p [:em "trlisp"] " is an implementation of tree calculus with Lisp's compact,
 uniform grammar. The element at the head of a list, the function position, is
 invoked, with the elements of the tail of the list providing the arguments."]

 [:p "Unlike traditional Lisps, trlisp functions may be transparently decomposed
 so that the function may be inspected, analyzed, compared, optimized,
 decomposed, and modified, without the need for quotation. That is to say,
 trlisp functions are reflective."]

 [:p [:em "trlisp"] " is intended to be used to explore practical questions such
 as"

  [:ul
   [:li "How useful is it to have unquoted access to the contents of
a function?"]

   [:li "Can new functions be composed from pulled-apart pieces of others?"]

   [:li "Could a function be automatically inverted?"]]]
 ]

