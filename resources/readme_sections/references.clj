[:section#references
 [:h2 "References"]

 [:h3 "Tree calculus references"]

 [:ul
  [:li
   [:p "Barry Jay's "
    [:a {:href "https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf"}
     "Reflective Programs in Tree Calculus"]
    " (pdf, 2021)"]

   [:p "trlisp's primary reference. See also the "
    [:a {:href "https://github.com/barry-jay-personal/tree-calculus"}
     "other materials"]
    " in Jay's GitHub page, particularly the Coq proofs."]]

  [:li
   [:p "Johannes Bader's "
    [:a {:href "https://treecalcul.us/"}
     "Tree Calculus"]
    " page"]

   [:p
    "Tree calculus advocacy, examples, and interactive demos. Note: The
 application rules are different than here, but the systems are equivalent.
 Bader also wrote a nice "
    [:a {:href "https://olydis.medium.com/a-visual-introduction-to-tree-calculus-2f4a34ceffc2"}
     "introduction blog post"]
    "."]]

  [:li
   [:p "Timur Latypoff's "
    [:a {:href "https://latypoff.com/tree-calculus-visualized/"}
     "Tree calculus, visualized"]]]

  [:li
   [:p
    "James Eversole's "
    [:a {:href "https://git.eversole.co/James/tricu"}
     "tricu"]]
   [:p "Tree calculus implemented in Haskell."]]]

 [:h3 "Other references"]

 [:ul
  #_[:li
     [:p "Wikipedia article on "
      [:a {:href "https://en.wikipedia.org/wiki/Fexpr"}
       "F-expressions"]]

     [:p "A concept that seems adjacent to decomposable functions."]]

  #_[:li
     [:p "Kent Pitman's "
      [:a {:href "https://www.nhplace.com/kent/Papers/Special-Forms.html"}
       "Special forms in Lisp"]]

     [:p "Discusses problems with Fexprs."]]

  #_[:li
     [:p "Mitchell Wand "
      [:a {:href ""}
       "The Theory of Fexprs is Trivial"]
      " (ps, 1998)"]]

  #_[:li
     [:p "John Shutt's "
      [:a {:href "https://web.cs.wpi.edu/~jshutt/kernel.html"}
       "Kernel Programming Language"]]

     [:p "See also Shutt's blog post on "
      [:a {:href "https://fexpr.blogspot.com/2011/04/fexpr.html"}
       "F-expressions"]
      "."]

     [:p "Discussions: "
      [:ul
       [:li
        [:a {:href "http://lambda-the-ultimate.org/node/3640"}
         "Lisps, first-class special forms, Fexprs, the Kernel Programming
 Language"]]

       [:li
        [:a {:href "http://lambda-the-ultimate.org/node/3861"}
         "First-class environments"]]

       [:li
        [:a {:href "http://lambda-the-ultimate.org/node/4093"}
         "Fexprs as the basis of Lisp function application; or, $vau: the
 ultimate abstraction"]]]]]

  [:li
   [:p "Palmer, Filardo, & Wu "
    [:a {:href "https://www.cs.swarthmore.edu/~zpalmer/publications/intensional-functions.pdf"}
     "Intensional functions"]
    " (pdf, 2024)"]

   [:p "Discusses how intensional functions offer non-application operations,
 such as comparison, serialization, hashing, etc." ]]
  ]
 ]
