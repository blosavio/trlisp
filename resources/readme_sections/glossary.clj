[:section#glossary
 [:h2 "Glossary"]

 [:dl
  [:dt#apply "apply"]
  [:dd
   [:p "Consume two trees, yielding a single tree according to the tree
 calculus rules. In trlisp, we "
    [:code "apply"]
    " by evaluating a list with a function (a "
    [:a {:href "#tree"} "tree"]
    ") at the head and an argument (also a tree) at its tail."]

   [:p "An instance of applying one tree to another is an "
    [:em "application"]
    ". trlisp provides six distinct applications, selected by the branching
 pattern of the tree in the function position."]]

  [:dt#argument "argument"]
  [:dd
   [:p "A tree appearing in the tail of a list to be evaluated. The argument is
 consumed in the course of applying the function."]

   [:p " Note: All trees may serve as an argument, even trees that are
 semantically functions, such as "
    [:code "Inc"]
    ". In other words, trees are first-class functions and may be passed as
 values and manipulated."]]

  [:dt#define "define"]
  [:dd [:p "Bind a name (a Clojure symbol) to tree. That name evaluates to the
 original tree wherever it appears in an expression."]]

  [:dt#fork "fork"]
  [:dd [:p "A node with exactly two children."]]

  [:dt#function "function"]
  [:dd
   [:p "A tree appearing at the head of a list to be evaluated. The function's
 branching pattern determines which application rule to invoke."]

   [:p "Note: All trees are potentially functions, i.e., any tree may
 competently invoke one of the applications."]]

  [:dt#leaf "leaf"]
  [:dd [:p "A node with exactly zero children."]]

  [:dt#node "node"]
  [:dd [:p "The basic unit of an unlabelled, binary tree. May be either a leaf,
 stem, or fork."]]

  [:dt#stem "stem"]
  [:dd [:p "A node with exactly one child."]]

  [:dt#tree "tree"]
  [:dd [:p "An arrangement of only "
        [:a {:href "#leaf"} "leafs"]
        ", "
        [:a {:href "#stem"} "stems"]
        ", and "
        [:a {:href "#fork"} "forks."]]]
  ]]
