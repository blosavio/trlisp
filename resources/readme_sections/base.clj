[:section#base
 [:h2 "Base functions"]

 [:p "Now we'll discuss functions that might be useful for solving day-to-day
 problems. We should always keep in mind that these functions are "
  [:a {:href "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L129"}
   "defined"]
  " (almost) solely in terms of "
  [:code "Δ"]
  ", or other trees themselves defined with "
  [:code "Δ"]
  ", a key characteristic of tree calculus."]

 [:h3#Boolean "Booleans"]

 [:p
  "The two Boolean values behave like Church Booleans, with "
  [:code "True"]
  " returning the first of two arguments, and "
  [:code "False"]
  " returning the second of two arguments."]

 [:pre
  [:code "(True x y) ;; => x"] [:br]
  [:code "(False x y) ;; => y"]]

 [:p
  "In addition to "
  [:code "Not"]
  ", trlisp provides the usual suspects: "
  [:code "And"] ", "
  [:code "Or"] ", "
  [:code "Implies"] ", and "
  [:code "Iff"]
  ". Here's just a sampling."]

 [:pre
  (replace-booleans (print-form-then-eval "(And True (Not True))")) [:br]
  (replace-booleans (print-form-then-eval "(Or True False)")) [:br]
  (replace-booleans (print-form-then-eval "(Iff False False)")) [:br]
  (replace-booleans (print-form-then-eval "(Implies True False)"))]

 [:p "See the "
  [:a {:href "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/test/tree_calculus/definitions_test.clj#L201-L236"}
   "testing namespace"]
  " for the complete truth tables. "]

 [:h3#arithmetic "Arithmetic"]

 [:p "Similar to Church encoding, tree calculus represents natural numbers as
 repeated application of "
  [:code "K"]
  " such that number "
  [:em "n"]
  " is represented by "
  [:em "K" [:sup "n"] "Δ"]
  ". Let's formulate the first few numbers and visualize them."]

 [:table
  [:tr
   [:th "number"]
   [:th "tree"]
   [:th "visualization"]]

  [:tr
   [:td "Zero"]

   [:td [:pre [:code "Δ"]]]

   [:td [:pre [:code (make-nat-number 0)]]]]

  [:tr
   [:td "One"]

   [:td [:pre [:code "(K Δ)"]]]

   [:td [:pre [:code (make-nat-number 1)]]]]

  [:tr
   [:td "Two"]

   [:td [:pre [:code "(K (K Δ))"]]]

   [:td [:pre [:code (make-nat-number 2)]]]]

  [:tr
   [:td "Three"]

   [:td [:pre [:code "(K (K (K Δ)))"]]]

   [:td [:pre [:code (make-nat-number 3)]]]]

  [:tr [:td "And so on."]]]

 [:p "It's obnoxious to have to bash those out on the keyboard, so the "
  [:a {:href "https://blosavio.github.io/trlisp/tree-calculus.utilities.html"}
   "utility namespace"]
  " provides a pair of functions to interconvert integers and trees."]

 [:pre
  (print-form-then-eval "(nat->tree 2)") [:br] [:br]
  (print-form-then-eval "(tree->nat (K (K Δ)))")]

 [:p "All the following functions require us to supply the arguments as the tree
 representation, not the plain integers, so we'll be replying on "
  [:code "nat->tree"]
  " and "
  [:code "tree->nat"]
  "."]

 [:p "trlisp provides the classic "
  [:em "increment"]
  " and "
  [:em "decrement"]
  " functions. Let's calculate what arrives after integer "
  [:em "One"]
  "."]

 [:pre
  (print-form-then-eval "(Successor [[] []])")]

 [:p "That's…not excellent. The argument and return value are a pile of
 brackets."]

 [:p "Let's insert the "
  [:code "nat->tree"]
  " and "
  [:code "tree->nat"]
  " utilities to convert the argument and return value trees back to plain
 integers."]

 [:p "Clojure provides a handy threading macro so we don't have to read the
 expression from the inside out."]

 [:pre (print-form-then-eval "(-> 2
                                nat->tree
                                Successor
                                tree->nat)")]

 [:p "That's a tad better."]

 [:p " We feed plain integer "
  [:code "2"]
  " into "
  [:code "nat->tree"]
  ", which converts it into the tree representation "
  [:code (str (nat->tree 2))]
  ". Then, we feed that tree into "
  [:code "Successor"]
  ", which does its calculation, returning yet another tree. Finally, we feed
 that tree into "
  [:code "tree->nat"]
  ", which hands us an understandable integer "
  [:code "3"]
  ", which, since we paid attention in school, we have good reason to believe is
 the correct answer."]

 [:p "trlisp also provides the companion function, "
  [:code "Predecessor"]
  "."]

 [:pre (print-form-then-eval "(-> 5
                                nat->tree
                                Predecessor
                                tree->nat)")]

 [:p "trlisp provides functions for addition, subtraction, multiplication, and
 division."]

 [:p "Let's demonstrate addition by first binding trees to "
  [:code "Two"]
  " and "
  [:code "Three"]
  "."]

 [:pre
  (print-form-then-eval "(def Two (nat->tree 2))")
  [:br]
  (print-form-then-eval "(def Three (nat->tree 3))")
  [:br]
  [:br]
  (print-form-then-eval "(-> (Plus Two Three)
                               tree->nat)")]

 [:p "Yup. Adding two to three results in five."]

 [:p "Beware: the "
  [:code "Divide"]
  " function is doubly-recursive, and performs poorly."]

 [:h3#structures "Lists and tuples"]

 [:p "trlisp implements "
  [:em "2-tuples"]
  " with "
  [:code "Pair"]
  " and accessor functions "
  [:code "First"]
  " and "
  [:code "Second"]
  "."]

 [:pre
  (print-form-then-eval "(Pair Two Three)")
  [:br]
  [:br]
  (print-form-then-eval "(-> (Second (Pair Two Three))
                               tree->nat)")]

 [:p "Tree calculus lists are serial forks with the values "
  [:em "v" [:sub "n"]]
  " in the left branches."]

 [:pre [:code (str "  Δ" "\n"
                   " / \\" "\n"
                   "v0  Δ" "\n"
                   "   / \\" "\n"
                   "  v1  Δ" "\n"
                   "     / \\" "\n"
                   "    v2  Δ" "\n"
                   "       / \\" "\n"
                   "      …   …")]]

 [:p "trlisp provides "
  [:code "List"]
  " to quickly construct a list from its arguments."]

 [:p [:code "Bite"] " constructs an "
  [:em "8-tuple"]
  " of bits, while strings are constructed with "
  [:code "String"]
  " from Bites representing the characters' "
  [:span.small-caps "ascii"]
  " bytecodes."]

 [:p "Append to lists with "
  [:code "Append"]
  " and reverse a trlisp list with "
  [:code "Reverse"]
  "."]

 [:h3#folding "Mapping and folding"]

 [:p "Tree calculus defines functions for operating on elements of a list.
 trlisp implements a "
  [:em "map"]
  "ping function that applies a function to every element of a list a returns
 another list."]

 [:p "Let's construct a list of integers."]

 [:pre (print-form-then-eval "(def our-list (List (nat->tree 2)
                                                  (nat->tree 3)
                                                  (nat->tree 4)))")]

 [:p "Pretend we'd like to increment, in-place, each of those integers. The "
  [:code "Successor"]
  " function makes a solid choice for incrementing."]

 [:p "The function signature for mapping is"]

 [:pre [:code "(Map " [:em "function list"] ")"]]

 [:p [:code "Map"]
  " returns a tree, which is difficult to decipher. So we'll use "
  [:code "List->seq"]
  " to convert the tree calculus list into a Clojure sequence of tree integers,
 and then Clojure's "
  [:code "map"]
  " with "
  [:code "tree->nat"]
  " to convert each tree integer into a plain integer."]

 [:pre (print-form-then-eval "(->> (Map Successor our-list)
                                   List->seq
                                   (map tree->nat))")]

 [:p "Excellent. "
  [:code "Map"]
  " incremented three integers contained in our list."]

 [:p "trlisp also implements tree calculus' "
  [:em "fold"]
  "ing operations. The function signature for left-folding is"]

 [:pre [:code "(Fold-Left " [:em "function init list"] ")"]]

 [:p "Let's compose those arguments. We'll use "
  [:code "Plus"]
  " since it's easy to eyeball it's effects. We'll assign a tree to an initial
 value."]

 [:pre (print-form-then-eval "(def our-init (nat->tree 1))")]

 [:p "We'll use the same list of integers from earlier, "
  [:code "our-list"]
  "."]

 [:p "Now, we invoke the fold, and convert the result to an integer we can
 recognize."]

 [:pre (print-form-then-eval "(-> (Fold-Left Plus our-init our-list)
                                  tree->nat)")]

 [:p [:code "Fold-Left"]
  " adds one to two, then that result to three, then that result to four,
 yielding ten."]

 [:p [:code "Fold-Right"]
  " works analogously."]

 [:h3#tags "Tagging"]

 [:p "Tree calculus defines a system for tagging values, removing tags,
 reading the tags, retrieving the values, and for applying tagged functions
 to tagged values."]

 [:p "Based on those tagging utilities, tree calculus defines a simple type
 system that tags a tree with a type, which can later be checked during
 application."]

 [:p "trlisp implements the both the tagging and type-check systems, but they're
 not user-friendly, nor comprehensive, so we won't discuss them further."]

 [:h3#tree-analysis "Tree analysis"]

 [:p "Tree calculus' tentpole feature is the ability of one function to inspect
 another function directly, without quoting. This feature is enabled by the fact
 that all entities, functions and values, are composed of the same stuff:
 trees."]

 [:p "trlisp implements several functions in this category, such as measuring
 the size of a function, determining the equality of two functions, and running
 generic queries on the branching structure."]

 [:p "For example, we can use the "
  [:code "Size"]
  " function to measure the size of the K combinator."]

 [:pre (print-form-then-eval "(-> (Size K)
                                  tree->nat)")]

 [:p "Yes, the K combinator is composed of two nodes as we expect."]

 [:p "We could also ask if two functions are equal. Let's see if the K
 combinator is equal to one leaf node descendant from another node."]

 [:pre (replace-booleans (print-form-then-eval "(Equal? K (Δ Δ))"))]

 [:p "Yes, the two trees are equal as we expect."]

 [:p "Tree calculus' "
  [:em "triage"]
  " defines a basic system for testing for leaves, stems, or forks. With triage
 in hand, tree calculus goes on to define "
  [:em " pattern matching"]
  ". As I understand it, pattern-matching answers the following question."]

 [:p "Given tree "
  [:em "A"]
  ","]

 [:pre [:code (make-fork "Δ" "foo")]]

 [:p "and some target component of "
  [:em "A"]
  ","]

 [:pre [:code "foo"]]

 [:p "and test tree "
  [:em "B"]
  ","]

 [:pre [:code (make-fork "Δ" "baz")]]

 [:p "What is the "
  [:em "thing"]
  " located in tree "
  [:em "B"]
  " at the same location as "
  [:code "foo"]
  " in tree "
  [:em "A"]
  "? The answer is "
  [:code "baz"]
  "."]

 [:p "trlisp implements both triage and pattern matching, and they pass some
 rudimentary unit tests, but I am not confident the tests are correct nor
 sufficient."]

 [:p "At any rate, we "
  [:em "should"]
  " be impressed with the fact that all these functions, sophisticated as they
 are, are "
  [:a {:href "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/src/tree_calculus/definitions.clj#L399"} "defined"]
  " in terms of the basic node operator."]

 [:h3#evaluators "Self evaluators"]

 [:p "Tree calculus demonstrates its ability for programs to act on themselves
 by defining four self-evaluators. trlisp implements all four: "
  [:em "Branch-first"]
  ", "
  [:em "Root"]
  ", "
  [:em "Root-and-branch"]
  ", and "
  [:em "Root-first"]
  " evaluate their arguments with different strategies for inspecting the
 interior structure of their arguments."]

 [:p "See the "
  [:a {:href "https://github.com/blosavio/trlisp/blob/0f9f83904ebf16b54e8429ab5559aebce6e00b77/test/tree_calculus/definitions_test.clj#L698-L884"}
   "testing namespace"]
  " for example usage."]
 ]