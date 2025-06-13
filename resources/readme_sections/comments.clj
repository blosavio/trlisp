[:section#comments
 [:h2 "Comments"]

 [:p "Two of tree calculus' ideas intrigue me the most."]

 [:ol
  [:li
   [:strong "Every entity is composed of the same stuff."]

   [:p "In this regard similar to lambda calculus, I find it interesting that a
 complete computing system can be made when functions and values are composed
 from a single basic unit. In tree calculus' case, it's the "
    [:code "Δ"]
    " operator. Don't miss the impressive accomplishment: All the functions
 and values introduced here are defined with various trees composed of "
    [:code "Δ"]
    ". trlisp, via Clojure, only provides variable binding and the most minimal
 machinery for evaluating expressions. Tree calculus does all the actual
 computation."]]

  [:li
   [:strong "One operator plus six application rules."]

   [:p "Tree calculus' minimalism hints that it could be implemented in
 constrained environments, maybe a 6502 microprocessor, or a punch card machine,
 or perhaps even a marble machine."]]]

 [:p "I wrote trlisp so that I could evaluate tree calculus expressions to check
 my pencil and paper work, and to feel what it's like to work with. In that
 regard, trlisp provides that. While working through the exercises, trlisp can
 quickly evaluate an expression that otherwise takes multiple sheets of paper
 when written by hand."]

 [:p "On the other hand, it is immediately apparent that trlisp is in no way a
 practical, general-purpose programming language. Tree calculus appears to be
 firmly in the academic realm, as its current formulation emphasizes features
 important to academia: proofs of correctness and completeness, etc. All inputs
 must first be converted to a tree. Every tree returned from a function must be
 converted back to a value that is recognizable. And causes of incorrect results
 are tedious to track down."]

 [:p "Finally, the fact that functions and values are all the same 'type', i.e.,
 trees, makes some expressions nonsensical."]

 [:pre [:code (replace-nat-number (print-form-then-eval "(Three K)"))]]

 [:p "For example, due to the way numbers are encoded into trees, a number "
  [:em "n"]
  " applied to anything always returns "
  [:em "n-1"]
  " by the fork-leaf rule."]

 [:p "To implement a practical language, we'd need to immediately develop an
 account for at least strong typing (but not necessarily static typing),
 name-to-value binding, error-handling, and heterogeneous composite data
 structures (the features currently provided by trlisp's host, Clojure). Soon
 after that, we'd need to define performant versions of functions that operate
 on the generally necessary fundamental data types (i.e, arithmetic in binary
 integers and floats, substitution on strings, etc.) At that point, we'd be
 replicating Clojure, which wisely delegates much of that to Java. A monumental
 undertaking, but possibly rewarding."]

 [:h3#implications "Implications"]

 [:p "It's not obvious to me how someone would integrate tree calculus' node
 operator and application rules to an already existing programming language, or
 even if there would be advantages in doing so. And we've just discussed the
 formidable challenge of writing one from the ground up."]

 [:p "On the other hand, having direct access to a function's definition does
 suggest some interesting possibilities. Being able to inspect, analyze,
 optimize, modify, and borrow pieces from a function could enable tasks that are
 not currently easy, or even possible. In recent memory, I have at least "
  [:a {:href "https://blosavio.github.io/speculoos/speculoos.utility.html#var-defpred"}
   "one utility"]
  " that would have been faster and more elegant to write if I had run-time (not
 compile-time) access to the function's definition. I wouldn't think it's a
 capability we'd use often, but it could be nice tool in the toolbox. Something
 akin to reluctantly writing a Lisp macro."]
 ]