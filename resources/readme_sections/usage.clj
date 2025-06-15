[:section#usage
 [:h2 "Usage"]

 [:h3 "Lisp-y"]

 [:section#lisp

  [:p [:em "trlisp"] " inherits its evaluation model from its host, Clojure, a
 Lisp dialect. In Lisp, the fundamental evaluation unit is a list enclosed in
 parentheses. The first element of the list is the function, and any trailing
 list elements are the function's arguments, like this."]

  [:pre [:code "(function argument-1 argument-2 ... argument-n)"]]

  [:p "If we'd like to sum the integers two and three, we'd write this."]

  [:pre [:code  "(+ 2 3)"]]

  [:p "In this example, the parentheses contain the expression we'd like to
 evaluate. The symbol for addition, "
   [:code "+"]
   ", is the first
 element, so it serves as the function. The trailing elements, integers "
   [:code "2"]
   " and "
   [:code "3"]
   ", supply the two arguments to "
   [:code "+"]
   "."]

  [:p "When we evaluate the expression " [:code  "(+ 2 3)"] ", we obtain a
 value, integer " [:code "5"] "."]

  [:pre (print-form-then-eval "(+ 2 3)")]

  [:p "trlisp follows this model. Parenthesized lists form a unit of evaluation,
 with the function at the head of the list applying to zero or more arguments
 in the tail."]]

 [:h3 [:code "Δ"] ", the do-everything operator"]

 [:p "A notable aspect of tree calculus is that every "
  [:em "thing"]
  ", functions as well as values, is composed of a single, basic unit, "
  [:code "Δ"]
  ". So we must continually keep in mind that there is not a strict delineation
 between tree calculus functions, such as the "
  [:code "+"]
  " function, and tree calculus scalar values, such as integer "
  [:code "3"]
  ". Every entity in tree calculus is a binary tree composed only of different
 patterns of "
  [:code "Δ"]
  ". The application rules govern how one or two trees produce another tree."]

 [:p "Tree calculus defines six application rules. Five rules are the result
 of dispatching on whether the tree in the function position of the list is a
 leaf, stem, or fork, the last of which further dispatches on whether the left
 child is a leaf, stem, or fork."]

 [:p "The sixth rule is merely the degenerate case with zero arguments, which
 we'll discuss first."]

 [:h4#apply-leaf-to-nothing "Rule 0: Applying a leaf to nothing"]

 [:p "Let's imagine a picture of the first rule: applying a leaf to nothing."]

 [:pre [:code "Δ"]]

 [:p "It doesn't look like much. Just a Greek delta character floating on the
 page, but as the rules progress and we add more elements, our picture will
 become more informative."]

 [:p "Applying the "
  [:code "Δ"]
  " operator to zero arguments returns merely itself. We put the node symbol in
 function position of the list, followed by zero arguments, and evaluate the
 expression."]

 [:pre [:code "(Δ)"]]

 [:p "This returns a single tree node with zero children."]

 [:p "Often, a function definition will involve a bare leaf in the middle of a
 sequence of applications, so those parentheses might get visually distracting.
 If we only need a bare leaf, we may skip the parentheses."]

 [:pre (print-form-then-eval "(= (Δ) Δ)")]

 [:p "We can see that "
  [:code "(Δ)"]
  " and "
  [:code "Δ"]
  " are equivalent."]

 [:h4#apply-leaf-to-something "Rule 1: Applying a leaf to something"]

 [:p "Our picture grows with Rule 1. Since we are applying a leaf to something,
 we'll imagine a leaf as before, plus something else standing to its right.
 Let's call it "
  [:code "z"]
  "."]

 [:pre [:code "Δ     z"]]

 [:p [:code "z"] " will be some unspecified tree that can be operated on,
 either another leaf, a stem, or some unspecified fork."]

 [:p "To make our picture exceedingly explicit, let's introduce a symbol "
  [:code "␣"]
  " to visually separate the function and the argument."]

 [:pre [:code "Δ  ␣  z"]]

 [:p "We can interpret that picture as "
  [:em "Function "
   [:code "Δ"]
   " applied to argument "
   [:code "z"]
   "."]]

 [:p "As with all Lisps, trlisp uses parentheses for function invocation. To
 evaluate that expression, we put the function "
  [:code "Δ"]
  " at the head of the list and the single argument "
  [:code "z"]
  " in the tail."]

 [:pre [:code "(Δ z)"]]

 [:p "A Lisp programmer would read that expression as "
  [:em "Function "
   [:code "Δ"]
   " applied to argument "
   [:code "z"]]
  ", which is the same interpretation we just gave our picture."]

 [:p "When the " [:code "Δ"] " function is evaluated with one argument, a stem
 results with the argument forming the single child branch. Here is a picture of
 the result."]

 [:pre [:code (make-stem "z")]]

 [:p "The expression returns a stem: a root node at the top, with a single child
 element "
  [:code "z"]
  " branching straight off its bottom."]

 [:h4#apply-stem "Rule 2: Applying a stem"]

 [:p "Let's visualize applying a stem to something."]

 [:pre [:code (make-stem "x" "z")]]

 [:p "The stem, serving as our function, is on the left and applies to the
 argument immediately to its right, the "
  [:code "z"]
  ", which may be any valid tree."]

 [:p "To evaluate this in trlisp, we put a stem in the function position and the
 argument in the tail. Let's take it step by step. To compose the function, a
 stem, we'll recall the "
  [:a {:href "#apply-leaf-to-something"} "previous subsection"]
  " where we constructed a stem by applying a leaf node to something."]

 [:pre [:code "(Δ x)"]]

 [:p "The stem we just now composed consists of a root node at top and an
 unspecified child tree "
  [:code "x"]
  "."]

 [:p "Now we have a stem in hand to serve as a function. We place that stem at
 the head of our list, followed by the argument, some unspecified tree "
  [:code "z"]
  "."]

 [:pre [:code "((Δ x) z)"]]

 [:p "Tree calculus declares that evaluating that expression yields a fork,
 with "
  [:code "x"]
  " forming the left child branch and "
  [:code "z"]
  " forming the right child branch."]

 [:p "We could visualize the result like this."]

 [:pre [:code (make-fork "x" "z")]]

 [:p "Root node at top and two children, in order, branching off the bottom."]

 [:p "An "
  [:a {:href "#variadic"} "upcoming section"]
  " discusses ways to write this more efficiently."]

 [:p "Note that application Rules 0, 1, and 2 form a basis for
 constructing trees. Applying a bare leaf to nothing returns a leaf. Applying a
 leaf to something creates a stem. And applying a stem creates a fork."]

 [:p "Application Rules 3, 4, and 5 involve applying three varieties of
 forks. When applying a fork, trlisp dispatches on whether the left child itself
 is a leaf, stem, or fork."]

 [:h4#apply-fork-leaf "Rule 3: Applying a fork with left child leaf"]

 [:p "Here is a picture of applying a fork, where the left child is a leaf, to
 an argument "
  [:code "z"]
  "."]

 [:pre [:code (make-fork "Δ" "y" "z")]]

 [:p "Both the right child branch "
  [:code "y"]
  " and argument "
  [:code "z"]
  " can be any valid tree."]

 [:p "To express this application in trlisp, let's first construct the function,
 a fork. We know how to construct a fork from the "
  [:a {:href "#apply-stem"} "previous subsection"]
  ". A fork is constructed by applying a stem to something, and a stem itself is
 constructed by applying a leaf to something. We'll do each step in turn."]

 [:p "The left branch of our function is a leaf, so we need a root node and a
 child "
  [:code "Δ"]
  ". To construct a stem, we apply a leaf to another leaf."]

 [:pre [:code "(Δ Δ)"]]

 [:p "We visualize that resulting stem like this."]

 [:pre [:code (make-stem "Δ")]]

 [:p "Now that we have a stem, we know from Rule 2 that applying a stem to
 something constructs a fork. So we can apply that stem to some branch "
  [:code "y"]
  "."]

 [:pre [:code "((Δ Δ) y)"]]

 [:p "Which produces a tree that will serve as our function."]

 [:pre [:code (make-fork "Δ" "y")]]

 [:p "A root node at top, a leaf node is the left child, and some subtree "
  [:code "y"]
  " is the right child."]

 [:p "Finally, we insert this function at the head of the list and supply "
  [:code "z"]
  " as the argument in the tail position."]

 [:pre [:code "(((Δ Δ) y) z)"]]

 [:p "Tree calculus declares that if the left child of a fork is a leaf,
 application of that fork to anything returns the fork's right branch and
 discards the argument. In this case, the function's right child is "
  [:code "y"]
  ", which is returned. Argument "
  [:code "z"]
  " is discarded."]

 [:p "One way to look at this is the sequential application of a stem to two
 arguments "
  [:code "y"]
  ", then "
  [:code "z"]
  "."]

 [:pre [:code "Δ\n|  ␣  y  ␣  z\nΔ"]]

 [:p "This is a pattern that we see repeatedly in tree calculus: Applying a
 stem to two arguments selects the first of two arguments "
  [:code "y"]
  " and discards the second argument "
  [:code "z"]
  "."]

 [:p#forgiving "trlisp strives to be forgiving when it comes to parentheses. We
 can discard any pair of parentheses if the implied left-association holds. The
 following"]

 [:pre
  [:code "(((Δ Δ) y) z)"] [:br]
  [:code " ((Δ Δ) y  z)"] [:br]
  [:code " ((Δ Δ  y) z)"] [:br]
  [:code "  (Δ Δ  y  z)"]]

 [:p "are all equivalent: All return "
  [:code "y"]
  "."]

 [:h4#apply-fork-stem "Rule 4: Applying a fork with left child stem"]

 [:p "Here is a picture of a function composed of a fork with a left child
 stem."]

 [:pre [:code (make-fork-stem "x" "y" "z")]]

 [:p "Working up from the bottom, the left child stem is itself composed of a
 node with some unspecified tree "
  [:code "x"]
  " as it single child. To make this stem"]

 [:pre [:code (make-stem "x")]]

 [:p "we evaluate"]

 [:pre [:code "(Δ x)"]]

 [:p "Then, to make our desired fork, we supply that stem as the left child
 branch and some unspecified tree "
  [:code "y"]
  " as the right child branch."]

 [:pre [:code "(Δ (Δ x) y)"]]

 [:p "Which evaluates to this picture."]

 [:pre [:code (make-fork-stem "x" "y")]]

 [:p "See the "
  [:a {:href "#forgiving"} "note"]
  " at the end of the previous subsection on why we're allowed to sometimes drop
 parentheses."]

 [:p "Finally, we apply this function to some unspecified tree as argument "
  [:code "z"] " by inserting the function at the head of the list and the
 argument in the tail."]

 [:pre [:code "((Δ (Δ x) y) z)"]]

 [:p "Tree calculus declares this expression to evaluate to"]

 [:pre [:code "((y z) (x z))"]]

 [:p "The order of the function's right child "
  [:code "y"]
  " and left grandchild "
  [:code "x"]
  " are swapped, and the argument "
  [:code "z"]
  " is broadcast to each. Then the result of the first evaluation is applied to
 the result of the second evaluation. This pattern often appears in definitions
 because it's useful to be able to swap two items."]

 [:h4#apply-fork-fork "Rule 5: Applying a fork with left child fork"]

 [:p "The final application rule involves a function whose left fork is itself a
 fork. Let's visualize that situation."]

 [:pre [:code (make-fork-fork "w" "x" "y" "z")]]

 [:p "Examining the function on the left, we see a child fork with two branches,
 designated "
  [:code "w"]
  " and "
  [:code "x"]
  ". Using what we've discussed so far, let's construct this function from the
 bottom up. We know how to construct a fork, so let's make one with a left
 branch some unspecified "
  [:code "w"]
  " and right branch some unspecified "
  [:code "x"]
  "."]

 [:pre [:code "(Δ w x)"]]

 [:p "That trlisp expression creates this sub-tree."]

 [:pre [:code (make-fork "w" "x")]]

 [:p "With that sub-tree in hand, let's move up one level. That sub-tree becomes
 the left child of the root node, while some unspecified tree "
  [:code "y"]
  " becomes the right child node."]

 [:pre [:code "(Δ (Δ w x) y)"]]

 [:p "Now we have this picture."]

 [:pre [:code (make-fork-fork "w" "x" "y")]]

 [:p "This is our function, which we insert into the head of our trlisp
 expression, followed by some unspecified argument "
  [:code "z"]]

 [:pre [:code "((Δ (Δ w x) y) z)"]]

 [:p "to obtain our final expression."]

 [:p "Tree calculus declares that this expression evaluates to the following."]

 [:pre [:code "(z w x)"]]

 [:p "Applying a fork with a left child fork results in making the argument "
  [:code "z"]
  " the new function, with the decomposition of the function's left child's
 branches "
  [:code "w"]
  " and "
  [:code "x"]
  " the new arguments. This pattern is useful when we need to pull apart a fork
 and send the pieces to a function."]

 [:p "The next two subsections introduce a few conveniences."]

 [:h4#variadic "Variadic application"]

 [:p "trlisp provides a convenience to minimize parentheses: We may supply any
 number of arguments and trlisp will sequentially apply them by
 left-association. For example, when we evaluate the "
  [:code "Δ"]
  " function with two arguments, we obtain a fork. Let's supply arguments "
  [:code "x"]
  " and "
  [:code "y"]
  ", two unspecified trees."]

 [:pre [:code "(Δ x y)"]]

 [:p "When evaluated, this returns a fork, a node whose left child is some
 tree "
  [:code "x"]
  " and whose right child is "
  [:code "y"]
  ", some other tree. Under the hood, trlisp applied "
  [:code "Δ"]
  " to "
  [:code "x"]
  ", then applied that result to "
  [:code "y"]
  ", exactly as if we had evaluated"]

 [:pre [:code "((Δ x) y)"]]

 [:p "This principle can be extended as needed. Five applications"]

 [:pre [:code "(((((Δ Δ) Δ) Δ) Δ) Δ)"]]

 [:p "may be more succinctly written like this"]

 [:pre [:code "(Δ Δ Δ Δ Δ Δ)"]]

 [:p "with the understanding that application is left-associative."]

 [:h4 "Variable binding and convenience functions"]

 [:p "Usually, we'll find it convenient to forgo combinatory logic's goal of
 avoiding names. In those situations, we can rely on our host platform's
 variable binding to give meaningful names to commonly used trees. Clojure's "
  [:code "def"] " binds a name to a value."]

 [:pre (print-form-then-eval "(def foo (Δ Δ))")]

 [:p "Here, we bound the Clojure symbol "
  [:code "foo"]
  " to a two-node stem. Now we can use "
  [:code "foo"]
  " in future expressions, and it will evaluate to "
  [:code "(Δ Δ)"]
  "."]

 [:p "Also, we will sometimes find it convenient to use a lambda calculus-like
 expression. We can express"]

 [:pre [:code "λa.λb.ab"]]

 [:p "as"]

 [:pre [:code "(fn [a] (fn [b] (a b)))"]]

 [:p "in Clojure."]

 [:p "In trlisp , such a function will appear hanging off a tree somewhere. We
 might see something like this."]

 [:pre [:code (make-fork-fork "w"
                              "(fn [a] (fn [b] (a b)))"
                              "y"
                              "z")]]

 [:p "trlisp's application machinery handles that tree without any additional
 effort from us."]

 [:h4#application-summary "Application rules quick summary"]

 [:p "Here are all six application rules at once. Notice that the first three
 rules describe how to construct a leaf, stem, or fork. The last three rules
 describe how to evaluate the application of a fork with a left child leaf, a
 left child stem, and left child fork, respectively."]

 [:table
  [:tr
   [:td [:p "0. Apply leaf to nothing."]]
   [:td [:pre [:code  "Δ ␣ "]]]
   [:td [:pre [:code " (Δ)      = Δ"]]]]

  [:tr
   [:td [:p "1. Apply leaf to something."]]
   [:td [:pre [:code  "Δ ␣ z"]]]
   [:td [:pre [:code "((Δ)   z) = (Δ   z)"]]]]

  [:tr
   [:td [:p "2. Apply stem."]]
   [:td [:pre [:code (make-stem "x" "z")]]]
   [:td [:pre [:code "((Δ x) z) = (Δ x z)"]]]]

  [:tr
   [:td [:p "3. Apply fork-leaf."]]
   [:td [:pre [:code (make-fork "Δ" "y" "z")]]]
   [:td [:pre [:code "((Δ  Δ      y) z) = y"]]]]

  [:tr
   [:td [:p "4. Apply fork-stem."]]
   [:td [:pre [:code (make-fork-stem "x" "y" "z")]]]
   [:td [:pre [:code "((Δ (Δ   x) y) z) = ((y z) (x z))"]]]]

  [:tr
   [:td [:p "5. Apply fork-fork."]]
   [:td [:pre [:code (make-fork-fork "w" "x" "y" "z")]]]
   [:td [:pre [:code "((Δ (Δ w x) y) z) = (z w x)"]]]]]

 [:p "And that's it. One operator and six rules are all that is necessary for a
 combinatorially-complete system."]

 [:h3#implementation "Implementation Note"]

 [:p "Tree calculus trees have a dual nature: they are both structures and
 functions. That is, trees "
  [:em "contain"]
  " an arbitrary pattern of descendants, but at the same time, they
 conceptually " [:em "apply"]
  " a transform to an argument. Any implementation must therefore capture these
 two properties."]

 [:p "Clojure vectors are handy because, while they are exceedingly capable at
 storing and retrieving arbitrary values, they support an interface that allows
 them to behave as a Lisp function. When a Clojure vector is placed in the
 function position of a list and an integer argument follows in the tail, the
 vector invokes an implied accessor function, "
  [:a {:href "https://clojure.github.io/clojure/clojure.core-api.html#clojure.core/nth"}
   [:code "nth"]]
  ", which returns the value at the index."]

 [:p
  "For example, we could explicitly access the third element with "
  [:code "nth"]
  " like so."]

 [:pre
  [:code ";; plain Clojure"] [:br]
  (print-form-then-eval "(nth [98 99 100] 2)")]

 [:p "Equivalently, we could rely on a Clojure vector's ability to behave as a
 Lisp function."]

 [:pre
  [:code ";; plain Clojure"] [:br]
  (print-form-then-eval "([98 99 100] 2)")]

 [:p "Putting a vector at the head of a list is a widely-used Clojure idiom."]

 [:p "Since Clojure hosts trlisp, vectors possess the machinery to serve as
 functions. We merely need to change that machinery from the accessor
 function "
  [:code "nth"]
  " to a different machine that implements tree calculus' application rules."]

 [:p "trlisp models a tree as an "
  [:a {:href "https://github.com/blosavio/thingy"}
   "altered Clojure vector"]
  " containing up to two other such altered vectors. These nested vectors
 provide the reified structure representing tree calculus' unlabelled binary
 trees. Since trlisp's vectors are altered so that the invocation machinery
 implements tree calculus' application rules, these nested vectors may serve as
 functions when placed at the head of a list."]

 [:p "A single node is simply an empty vector, i.e., it has no descendants. We
 can observe this by evaluating a bare node."]

 [:pre (print-form-then-eval "(Δ)")]

 [:p "Applying the "
  [:code "Δ"]
  " function to nothing returns trlisp's literal representation, an empty
 vector."]

 [:p "Let's consider a simple stem."]

 [:pre [:code (make-stem "Δ")]]

 [:p "We can construct a node with a single child leaf by applying one node to
 another node."]

 [:pre (print-form-then-eval "(Δ Δ)")]

 [:p "Now we see that the parent vector contains a single element, another
 vector of the same kind."]

 [:p "We could have equivalently written the following."]

 [:pre [:code "([] []) ;; => [[]]"]]

 [:p "If these were Clojure's normal vectors, evaluating that expression would
 result in an error. But since trlisp uses a special vector that implements tree
 calculus' application rules, it works fine."]

 [:p "The first special vector in the function position "
  [:code "[]"]
  " applied to another special vector "
  [:code "[]"]
  " invokes tree calculus' application rules. In this case, according to
 Rule 1, a leaf (empty vector) applied to another tree (also a leaf in this
 case), constructs a stem."]

 [:p "Extending this principle further, consider this simple fork."]

 [:pre [:code (make-fork "Δ" "Δ")]]

 "We can construct this fork by supplying the parent node with two child nodes."

 [:pre (print-form-then-eval "(Δ Δ Δ)")]

 [:p "Here we see that the root vector now contains two child vectors."]

 [:p "trlisp's main usefulness in exploring tree calculus is the fact that these
 special vectors invoke the tree calculus application rules instead of the
 built-in "
  [:code "nth"]
  "."]

 [:p "One practical consequence of using vectors this way is that rendering
 trees as nested empty vectors is not very ergonomic for human eyes. After some
 practice, it's not too difficult to mentally translate "
  [:code "(Δ Δ Δ)"]
  " to "]

 [:pre [:code (make-fork "Δ" "Δ")]]

 [:p "but it requires much more eyestrain to see that both are equivalent to"]

 [:pre [:code "[[] []]"]]

 [:p "And it only gets worse as the trees grow beyond three nodes."]

 [:p "One option is to use Clojure keywords to represent arbitrary subtrees.
 The evaluation of this expression"]

 [:pre (print-form-then-eval "(Δ :a :b)")]

 [:p "can be visualized like this."]

 [:pre [:code (make-fork :a :b)]]

 [:p "But we can't use this option when a Clojure keyword would end up in the
 function position of a tree application rule, like in Rule 4,"]

 [:pre
  [:code ";; this won't work"] [:br]
  [:code "((:a :b) (:c :d))"]]

 [:p "because Clojure keywords don't implement the tree calculus application
 rules."]

 [:p "In that case, we must define concrete trees to which to apply the rules."]

 [:pre
  (print-form-then-eval "(def h Δ)") [:br]
  (print-form-then-eval "(def i Δ)") [:br]
  (print-form-then-eval "(def j Δ)") [:br]
  (print-form-then-eval "(def k Δ)")
  [:br] [:br]
  (print-form-then-eval "((h i) (j k))")]

 (do
   (def h Δ)
   (def i Δ)
   (def j Δ)
   (def k Δ)
   nil)

 [:p "The expression can now return a valid value, but we are still left with
 the problem of figuring out what the tree "
  [:code (clojure.string/replace (str ((h i) (j k))) #" " " ")]
  " looks like."]

 [:p "trlisp doesn't currently (and may never) have a utility which renders tree
 diagrams. When walking through our discussion, I will silently replace things
 like "
  [:code (clojure.string/replace (str ((h i) (j k))) #" " " ")]
  " with its equivalent "
  [:code "(Δ Δ K)"]
  "."]

 [:p "Just keep in mind that if you are evaluating trlisp expressions on your
 own computer, you will see "
  [:code (clojure.string/replace (str ((h i) (j k))) #" " " ")]
  "."]

 [:p "To illustrate how function trees apply to arguments trees and return
 other trees, we can test for equality with the expected value. For example,
 Rule 2 tells us that a stem applied to an argument leaf returns a fork with
 the argument leaf as the right child."]

 [:pre (print-form-then-eval "((Δ Δ) Δ)" 9 9)]

 [:p "That's...okay. But working it out with pencil and paper, we expect it to
 return "
  [:code "(Δ Δ Δ)"]
  ". Is that the case?"]

 [:pre (print-form-then-eval "(= ((Δ Δ) Δ) (Δ Δ Δ))" 12 12)]

 [:p "Yup. The value returned from the original application, in the upper row,
 is equal to the value returned by the expression in the lower row. By directly
 comparing return values with "
  [:code "="]
  ", we don't have to squint at a pile of nested square brackets."]

 [:h3#conventions "Conventions"]

 [:p "Clojure names are "
  [:code "lower-case-with-hyphens"]
  ", such as "
  [:code "inc"]
  ", "
  [:code "first"]
  ", and "
  [:code "map"]
  ". trlisp names are "
  [:code "Upper-Case-With-Hyphens"]
  ", such as "
  [:code "Inc"]
  ", "
  [:code "First"]
  ", and "
  [:code "Map"]
  "."]

 [:p "The exceptions are:"
  [:ul
   [:li
    [:p [:code "d"]]
    [:p "The D combinator already claims the capital 'D'. Since the "
     [:code "d"]
     " function is used extensively in definitions, and it is so closely related
 to the D combinator, I chose to keep it as a single, lower-case 'd'."]]

   [:li
    [:p [:code "error"]]
    [:p "Java (Clojure's host) already uses "
     [:code "Error"]
     "."]]

   [:li
    [:p "Conversion utilities that accept Clojure types (which are lower-cased)
 and return trees. E.g., "]
    [:code "str->String"]
    "."]]]

 [:p "Java claims "
  [:code "Byte"]
  " while Clojure claims "
  [:code "byte"]
  ", so trlisp uses "
  [:code "Bite"]
  " to denote eight binary digits."]

 [:h3 "Recommendations"]

 [:p "First, work out the examples with a pencil and paper, and only then check
 their evaluations in trlisp. Letting the computer do all the work stymies a
 deeper understanding we might get by slinging around tree nodes by hand."]

 [:p "The "
  [:a {:href "https://github.com/blosavio/trlisp/blob/main/test/tree_calculus/definitions_test.clj"}
   "unit testing"]
  " namespace (and its "
  [:a {:href "https://github.com/blosavio/trlisp/blob/main/test/tree_calculus/exercising_test.clj"}
   "auxiliary"]
  ") contain working examples of every trlisp function."]

 [:p "Finally, the "
  [:a {:href "https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf"}
   "tree calculus book"]
  " is the authoritative and complete reference on the subject. This "
  [:em "ReadMe"]
  " is merely a description of how to use trlisp to evaluate tree calculus
 expressions. Use it as a companion to the "
  [:em "Tree Calculus Book"]
  "."]
 ]

