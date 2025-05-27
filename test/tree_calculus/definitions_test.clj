(ns tree-calculus.definitions-test
  "Tests for the basic tree-calculus definitions.

  To insert a unicode Delta:
  C-x 8, then ENTER, then type 0394"
  (:require
   [clojure.test :refer [are is deftest testing run-tests run-test]]
   [thingy.core :refer [assign-thingy-fn!
                        make-thingy]]
   [tree-calculus.definitions :refer :all]
   [tree-calculus.utilities :refer :all]))


(deftest Δ-tests
  (testing "equality with self"
    (are [x y] (= x y)
      (make-thingy) Δ
      (make-thingy :a) (Δ :a)
      (make-thingy :a :b) (Δ :a :b)))
  (testing "equality with clojure.lang.PersistentVector"
    (are [x y] (= x y)
      [] Δ
      [:a] (Δ :a)
      [:a :b] (Δ :a :b))))


(deftest something-is?-tests
  (are [x] (true? x)
    (leaf? Δ)
    (not (stem? Δ))
    (not (fork? Δ))

    (not (leaf? (Δ :a)))
    (stem? (Δ :a))
    (not (fork? (Δ :a)))

    (not (leaf? (Δ :a :b)))
    (not (stem? (Δ :a :b)))
    (fork? (Δ :a :b))))


(deftest appli-tests
  (testing "fn"
    (appli #(if % :correct) :foo)
    (appli (appli (appli K #(if % :correct)) nil) :foo))
  (testing "Rule 0a"
    (let [Rule0a (fn [a] (appli Δ a))
          A0 Δ
          A1 ΔΔ
          A2 ΔΔΔ]
      (are [x y z] (= x y z)
        (Δ A0) (Rule0a A0) ΔΔ
        (Δ A1) (Rule0a A1) (Δ ΔΔ)
        (Δ A2) (Rule0a A2) (Δ ΔΔΔ))))
  (testing "Rule 0b"
    (let [Rule0b (fn [a b] (appli (Δ a) b))
          A0 Δ      B0 Δ
          A1 Δ      B1 ΔΔ
          A2 Δ      B2 ΔΔΔ]
      (are [x y z] (= x y z)
        (Δ A0 B0) (Rule0b A0 B0) ΔΔΔ
        (Δ A1 B1) (Rule0b A1 B1) (Δ Δ ΔΔ)
        (Δ A2 B2) (Rule0b A2 B2) (Δ Δ ΔΔΔ))))
  (testing "Leaf Rule"
    (let [LeafRule (fn [a b] (appli (Δ Δ a) b))
          A0 Δ
          B0 :irrelevant

          A1 ΔΔ
          B1 :irrelevant

          A2 ΔΔΔ
          B2 :irrelevant]
      (are [x y z] (= x y z)
        A0 (LeafRule A0 B0) Δ
        A1 (LeafRule A1 B1) ΔΔ
        A2 (LeafRule A2 B2) ΔΔΔ)))
  (testing "Stem Rule"
    (let [StemRule (fn [a b c] (appli (Δ (Δ  a) b) c))
          A0 Δ
          B0 Δ
          C0 Δ

          A1 ΔΔ
          B1 ΔΔ
          C1 ΔΔ

          A2 ΔΔΔ
          B2 ΔΔΔ
          C2 ΔΔΔ]
      (are [x y z] (= x y z)
        ((A0 C0) (B0 C0)) (StemRule A0 B0 C0) (Δ Δ ΔΔ)
        ((A1 C1) (B1 C1)) (StemRule A1 B1 C1) ΔΔ
        ((A2 C2) (B2 C2)) (StemRule A2 B2 C2) ΔΔ)))
  (testing "Fork Rule"
    (let [ForkRule (fn [a b c d] (appli (Δ (Δ a b) c) d))
          W0 Δ
          X0 Δ
          Y0 :irrelevant
          Z0 Δ

          W1 ΔΔ
          X1 ΔΔ
          Y1 :irrelevant
          Z1 ΔΔ

          W2 ΔΔΔ
          X2 Δ
          Y2 :irrelevant
          Z2 ΔΔ

          W3 Not
          X3 False
          Y3 :irrelevant
          Z3 I]
      (are [x y z] (= x y z)
        ((Z0 W0) X0) (ForkRule W0 X0 Y0 Z0) ΔΔΔ
        ((Z1 W1) X1) (ForkRule W1 X1 Y1 Z1) ΔΔ
        ((Z2 W2) X2) (ForkRule W2 X2 Y2 Z2) ΔΔΔ
        ((Z3 W3) X3) (ForkRule W3 X3 Y3 Z3) True)))
  (testing "non-tree left child value"
    (is (thrown? Exception (appli (Δ (fn foo [_] ()) :bar) :baz)))))


(deftest mutl-appli-tests
  (are [x y] (= x y)
    Δ   (mult-appli Δ)
    ΔΔ  (mult-appli Δ Δ)
    ΔΔΔ (mult-appli Δ Δ Δ)
    Δ   (mult-appli Δ Δ Δ Δ)
    ΔΔ  (mult-appli Δ Δ Δ Δ Δ)
    ΔΔΔ (mult-appli Δ Δ Δ Δ Δ Δ)))


(deftest K-tests
  (let [x ΔΔΔ
        y :ignored]
    (are [a b] (= a b)
      x (K x y))))


(deftest I-tests
  (let [x ΔΔΔ]
    (are [a b] (= a b)
      x (I x))))


(deftest D-tests
  (let [x ΔΔ
        y ΔΔΔ
        z Δ]
    (are [a b] (= a b)
      ((y z) (x z)) (D x y z))))


(deftest d-tests
  (let [x ΔΔΔ]
    (are [x y] (= x y)
      (Δ (Δ x)) (d x))))


(deftest S-tests
  (let [x ΔΔ
        y ΔΔΔ
        z Δ]
    (are [a b] (= a b)
      ((x z) (y z)) (S x y z))))


(deftest B-tests
  (let [x I
        y Not
        z False]
    (are [a b] (= a b)
      (x (y z)) (B x y z))))


(deftest C-tests
  (let [x I
        y False
        z Not]
    (are [a b] (= a b)
      (x z y) (C x y z))))


(deftest W-tests
  (let [x K
        y :a]
    (are [a b] (= a b)
      (x y y) (W x y))))


(deftest True-tests
  (is (= :a (True :a :b))))


(deftest False-tests
  (is (= :b (False :a :b))))


(deftest And-tests
  (are [x y] (= x y)
    True  (And True  True)
    False (And True  False)
    False (And False True)
    False (And False False)))


(deftest Or-tests
  (are [x y] (= x y)
    True  (Or True  True)
    True  (Or True  False)
    True  (Or False True)
    False (Or False False)))


(deftest Not-tests
  (are [x y] (= x y)
    True (Not False)
    False (Not True)))


(deftest Implies-tests
  (are [x y] (= x y)
    True  (Implies True  True)
    False (Implies True  False)
    True  (Implies False True)
    True  (Implies False False)))


(deftest Iff-tests
  (are [x y] (= x y)
    True  (Iff True  True)
    False (Iff True  False)
    False (Iff False True)
    True  (Iff False False)))


(deftest Zero?-tests
  (are [x y] (= x y)
    True (Zero? (nat->tree 0))
    False (Zero? (nat->tree 1))
    False (Zero? (nat->tree 2))))


(deftest Pair-First-Second-tests
  (are [a b] (= a b)
    (Δ :x :y) (Pair :x :y)
    :x (First (Pair :x :y))
    :y (Second (Pair :x :y))))


(deftest Successor-tests
  (are [x] (= (inc x) (tree->nat (Successor (nat->tree x))))
    0 1 2))


(deftest Predecessor-tests
  (are [x] (= (dec x) (tree->nat (Predecessor (nat->tree x))))
    1 2 3))


(deftest tree-query-tests
  (are [a b] (= a b)
    True  (Leaf? Δ)
    False (Leaf? ΔΔ)
    False (Leaf? ΔΔΔ)

    False (Stem? Δ)
    True  (Stem? ΔΔ)
    False (Stem? ΔΔΔ)

    False (Fork? Δ)
    False (Fork? ΔΔ)
    True  (Fork? ΔΔΔ)))


(deftest Wait-tests
  (are [a b] (= a b)
    True  ((Wait And True) True)
    False ((Wait And True) False)
    False ((Wait And False) True)
    False ((Wait And False) False)))


(deftest Wait-1-tests
  (are [a b] (= a b)
    True  ((Wait-1 And) True True)
    False ((Wait-1 And) True False)
    False ((Wait-1 And) False True)
    False ((Wait-1 And) False False)))


(deftest Self-apply-tests
  (are [x] (= (x x) (Self-apply x))
    Δ
    ΔΔ
    ΔΔΔ))


(deftest Swap-tests
  (are [a b] (= a b)
    False (Implies True False)
    True ((Swap Implies) True False)

    True (Implies False True)
    False ((Swap Implies) False True)

    :x (K :x :y)
    :y ((Swap K) :x :y)))


(deftest Plus-tests
  (are [a b] (= (+ a b)
                (tree->nat (Plus (nat->tree a) (nat->tree b))))
    1 1
    1 2
    1 3

    2 1
    2 2
    2 3

    3 1
    3 1
    3 3))

(deftest Minus-tests
  (are [a b] (= (- a b)
                (tree->nat (Minus (nat->tree a) (nat->tree b))))
    3 2
    4 2
    5 2

    4 3
    5 3
    6 4

    5 4
    6 4
    7 4))


(deftest Times-tests
  (are [a b] (= (* a b)
                (tree->nat (Times (nat->tree a) (nat->tree b))))
    1 1
    1 2
    1 3

    2 1
    2 2
    2 3

    3 1
    3 2
    3 3))


(deftest Divide-tests
  (are [z] (= (/ 60 z)
              (tree->nat (Divide (nat->tree 60) (nat->tree z))))
    60 30 20 15 12 10 6 5 4 3 2 1))


(deftest List-tests
  (are [x y] (= x y)
    (List)
    []

    (List :a)
    [:a []]

    (List :a :b :c)
    [:a [:b [:c []]]]))


(deftest Bite-tests
  (is (thrown? clojure.lang.ArityException (Bite)))
  (are [x y] (= (apply Bite (map bit->Bit (repeat 8 x))) y)
    0 [[] [[] [[] [[] [[] [[] [[] [[] []]]]]]]]]
    1 [[[] []] [[[] []] [[[] []] [[[] []] [[[] []] [[[] []] [[[] []] [[[] []] []]]]]]]]]))


(deftest head-n-tail-tests
  (let [test-List (List (nat->tree 1)
                        (nat->tree 2)
                        (nat->tree 3))]
    (are [x y] (= x y)
      1 (tree->nat (t-head test-List))
      [2 3] (map tree->nat (List->seq (t-tail test-List))))))


(deftest t-cons-tests
  (are [x y] (= x y)
    (List :a :b :c :d)
    (t-cons :a (List :b :c :d))))


(deftest List-Map-tests
  (testing "empty lists"
    (are [x y] (= x y)
      t-nil (List-Map Successor t-nil)
      Δ (List-Map Successor (List))))
  (testing "non-emtpy lists"
    (are [x y z] (= (map tree->nat (List->seq (List-Map y x))) z)
      (List (nat->tree 99))
      I
      [99]

      (List (nat->tree 1)
            (nat->tree 2)
            (nat->tree 3))
      Successor
      [2 3 4])))


(deftest List-FoldLeft-tests
  (are [x y] (= x y)
    10
    (tree->nat (List-FoldLeft Plus (nat->tree 1) (List (nat->tree 2)
                                                       (nat->tree 3)
                                                       (nat->tree 4))))

    3
    (tree->nat (List-FoldLeft Divide (nat->tree 30) (List (nat->tree 2)
                                                          (nat->tree 5))))

    5
    (tree->nat (List-FoldLeft Divide (nat->tree 60) (List (nat->tree 3)
                                                          (nat->tree 4))))

    True
    (List-FoldLeft And True (List True True True))))


(deftest List-FoldRight-tests
  (are [x y] (= x y)
    99
    (tree->nat (List-FoldRight Plus (nat->tree 99) (List)))

    2
    (tree->nat (List-FoldRight Divide (nat->tree 1) (List (nat->tree 60)
                                                          (nat->tree 120)
                                                          (nat->tree 4))))

    True
    (List-FoldRight And True (List True True True))))


(deftest List-Append-tests
  (are [x y] (= x y)
    (List) (List-Append (List) (List))
    (List :a :b :c) (List-Append (List :a :b :c) (List))
    (List :a :b :c) (List-Append (List) (List :a :b :c))
    (List :a :b :c :d) (List-Append (List :a :b) (List :c :d))))


(deftest List-Reverse-tests
  (are [x y] (= x y)
    (List) (List-Reverse (List))
    (List :c :b :a) (List-Reverse (List :a :b :c))))


(deftest Size-tests
  (are [x y] (= x (tree->nat (Size y)))
    1 Δ
    2 (Δ Δ)
    3 (Δ Δ Δ)
    4 (Δ (Δ (Δ Δ)))
    5 (Δ (Δ Δ) (Δ Δ))))


(deftest Equal?-tests
  (testing "equal programs"
    (are [x] (= True (Equal? x x))
      Δ
      K
      (d Δ)
      I
      (K I)))
  (testing "not equal programs"
    (are [x y] (and (= False (Equal? x y))
                    (= False (Equal? y x)))
      Δ K
      Δ I
      K I
      K (K I)
      K (d Δ)
      (d Δ) (d (Δ Δ))
      (d K) (d (K K))
      (d (K I)) K)))


(deftest tagging-tests
  (testing "tag & get-tag"
    (are [x] (= :foo (Get-Tag (Tag :foo x)))
      K
      False
      I
      Plus))
  (testing "un-tagging"
    (are [x] (= x (Un-Tag (Tag :foo x)))
      K
      False
      I
      Plus))
  (testing "tag transparency"
    (are [x y] (= x y)
      3 (tree->nat ((Tag False Plus) (nat->tree 1) (nat->tree 2)))
      True ((Tag False And) True True)
      :foo ((Tag False I) :foo)
      [2 3 4] (map tree->nat (List->seq (List-Map  (Tag False Successor) (List (nat->tree 1) (nat->tree 2) (nat->tree 3))))))))


(def Plus-t (Y-t
             True
             (fn [m]
               (fn [plus]
                 ((Δ m I) (K (fn [x]
                               (fn [n] (Successor (plus x n))))))))))


(deftest fixpoint-tagging-tests
  (testing "tagged fixpoint function evaluation"
    (is (= 3 (tree->nat (Plus-t (nat->tree 1) (nat->tree 2))))))
  (testing "getting fixpoint function tag"
    (is (= True (Get-Tag Plus-t)))))


(deftest type-checking-and-typed-application
  (are [x y] (= x y)
    2 (tree->nat (Un-Tag (Typed-App (Tag True Successor) (Tag True (nat->tree 1)))))
    2 (tree->nat (Un-Tag (Typed-App (Tag False Successor) (Tag True (nat->tree 1)))))))


(deftest Stem?-2-tests
  (let [x Δ]
    (are [a b] (= a b)
      Δ (Stem?-2 Δ)
      (Δ (K (K Δ)) (x (K (K Δ)))) (Stem?-2 (Δ x))
      Δ (Stem?-2 (Δ :x :y)))))


(deftest Fork?-2-tests
  (are [a b] (= a b)
    (K K) (Fork?-2 Δ)
    (K (Δ :x (K (K Δ)))) (Fork?-2 (Δ (Δ :x)))
    Δ (Fork?-2 (Δ :x :y))))


(deftest Triage-tests
  (let [f0 K
        f1 Not
        f2 Plus
        q False
        one (K Δ)
        two (K (K Δ))]
    (are [a b] (= a b)
      f0 ((Triage f0 f1 f2) Δ)
      (f1 q) ((Triage f0 f1 f2) (Δ q))
      (f2 one two) ((Triage f0 f1 f2) (Δ one two)))))


(deftest Size-Variant-tests
  (are [x y] (= x (tree->nat (Size-Variant y)))
    1 Δ
    2 (Δ Δ)
    3 (Δ Δ Δ)
    4 (Δ (Δ (Δ Δ)))
    5 (Δ (Δ Δ) (Δ Δ))))


(deftest Equal?-Variant-tests
  (testing "equal programs"
    (are [x] (= True (Equal?-Variant x x))
      Δ
      K
      I
      (K I)))
  (testing "not equal programs"
    (are [x y] (and (= False (Equal?-Variant x y))
                    (= False (Equal?-Variant y x)))
      Δ K
      Δ I
      K I
      K (K I)
      (d (K I)) K)))


(deftest pattern-matching-tests
  (testing "exact match"
    (are [p s r x] (= ((Extension p s r) x)
                      s)
      :match :return-value :ignored :match))
  (testing "pattern: leaf, arg: leaf"
    (are [p s r x] (= ((Extension p s r) x)
                      s)
      Δ Δ :ignored Δ))
  (testing "pattern: leaf; arg: stem"
    (are [s r u] (= ((Extension Δ s r) (Δ u))
                    (r (Δ u)))
      :ignored Δ Δ
      :ignored Δ ΔΔ
      :ignored Δ ΔΔΔ))
  (testing "pattern: leaf; arg: fork"
    (are [s r t u] (= ((Extension Δ s r) (Δ t u))
                      (r (Δ t u)))
      :ignored Δ Δ Δ
      :ignored Δ ΔΔ ΔΔ
      :ignored Δ ΔΔΔ ΔΔΔ))
  (testing "pattern: stem; arg: leaf"
    (are [p s r] (= ((Extension (Δ p) s r) Δ)
                    (r Δ))
      Δ   :ignored Δ
      ΔΔ  :ignored ΔΔ
      ΔΔΔ :ignored ΔΔΔ))
  (testing "pattern: stem; arg: stem"
    (are [p s r u] (= ((Extension (Δ p) s r) (Δ u))
                      ((Extension p s (Δ K (K r))) u))
      Δ ΔΔΔ ΔΔΔ ΔΔΔ
      ΔΔ Δ Δ Δ
      ΔΔΔ Δ Δ Δ

      Δ ΔΔ ΔΔΔ (Δ (Δ ΔΔΔ ΔΔΔ) (Δ ΔΔΔ ΔΔΔ))
      (Δ (Δ ΔΔ ΔΔ) (Δ ΔΔ ΔΔ)) ΔΔ I (Δ (Δ ΔΔΔ ΔΔΔ) (Δ ΔΔΔ ΔΔΔ))))
  (testing "pattern: stem; arg: fork"
    (are [p s r t u] (= ((Extension (Δ p) s r) (Δ t u))
                        (r (Δ t u)))
      Δ   :ignored Δ   Δ    Δ
      ΔΔ  :ignored ΔΔ  ΔΔ  ΔΔ
      ΔΔΔ :ignored ΔΔΔ ΔΔΔ ΔΔΔ))
  (testing "pattern: fork; arg: leaf"
    (are [p q s r] (= ((Extension (Δ p q) s r) Δ)
                      (r Δ))
      Δ   Δ   :ignored Δ
      ΔΔ  ΔΔ  :ignored ΔΔ
      ΔΔΔ ΔΔΔ :ignored ΔΔΔ))
  (testing "pattern: fork; arg: stem"
    (are [p q s r u] (= ((Extension (Δ p q) s r) (Δ u))
                        (r (Δ u)))
      Δ   Δ   :ignored Δ   Δ
      ΔΔ  ΔΔ  :ignored ΔΔ  ΔΔ
      ΔΔΔ ΔΔΔ :ignored ΔΔΔ ΔΔΔ))
  (testing "pattern: fork; arg: fork"
    (are [p q s r t u] (= ((Extension (Δ p q) s r) (Δ t u))
                          ((Extension p
                                      (Extension q
                                                 (K s)
                                                 (Fork-Case-Aux-2 p))
                                      Fork-Case-Aux-1)
                           t u r))
      Δ  Δ  Δ Δ ΔΔ ΔΔ
      ΔΔ ΔΔ Δ Δ Δ  Δ

      Δ   Δ   Δ Δ ΔΔΔ ΔΔΔ
      ΔΔΔ ΔΔΔ Δ Δ Δ   Δ

      ΔΔ  ΔΔ  Δ Δ ΔΔΔ ΔΔΔ
      ΔΔΔ ΔΔΔ Δ Δ ΔΔ  ΔΔ)))


(deftest Eager-tests
  (are [m n] (= (n m) ((Eager m) n))
    Δ Δ
    ΔΔ ΔΔ
    ΔΔΔ ΔΔΔ

    Δ ΔΔ
    ΔΔ Δ

    Δ ΔΔΔ
    ΔΔΔ Δ

    ΔΔ ΔΔΔ
    ΔΔΔ ΔΔ))


(deftest On-Fork-tests
  (testing "leafs"
    (are [f0 f1 f2 node] (= ((On-Fork (Triage f0 f1 f2)) node)
                            (K node))
      I :irrelevant :irrelevant Δ
      K :irrelevant :irrelevant Δ))
  (testing "stems"
    (are [f0 f1 f2 node x] (= ((On-Fork (Triage f0 f1 f2)) (node x))
                              (K (node x)))
      :irrelevant I :irrelevant Δ Δ
      :irrelevant K :irrelevant Δ Δ))
  (testing "forks"
    (are [f0 f1 f2 node x y] (= ((On-Fork (Triage f0 f1 f2)) (node x y))
                                (((Triage f0 f1 f2) x) y))
      I :irrelevant :irrelevant Δ Δ Δ
      K :irrelevant :irrelevant Δ Δ Δ)))


(deftest Branch-First-Self-Evaluation-tests
  (testing "pass-through evaluation"
    (are [x] (= x (BF x))
      Δ
      ΔΔ
      ΔΔΔ
      (d Δ)))
  (testing "basic tree pattern application"
    (are [x y] (= (x y)
                  (BF x y))
      Δ Δ
      ΔΔ ΔΔ
      ΔΔΔ ΔΔΔ
      (d Δ) (d Δ)

      ΔΔ Δ
      Δ ΔΔ
      ΔΔΔ Δ
      Δ ΔΔΔ

      ΔΔ ΔΔΔ
      ΔΔΔ ΔΔ))
  (testing "semantic application"
    (are [x y] (= x y)
      K (BF I K)
      True (BF Not False)
      False (BF Leaf? ΔΔ)))
  (testing "fork lemma"
    (are [x y] (= (BF (Δ x y))
                  ((((Triage BF-Leaf (BF-Stem Eager) BF-Fork) x) y) BF))
      Δ Δ
      ΔΔ ΔΔ
      ΔΔΔ ΔΔΔ
      (d Δ) (d Δ)))
  (testing "fork-leaf lemma"
    (are [y z] (= y (BF (K y) z))
      Δ ΔΔ
      ΔΔ ΔΔΔ
      ΔΔΔ Δ))
  (testing "fork-stem lemma"
    (are [x y z] (= (BF (Δ (Δ x) y) z)
                    ((Eager
                      (BF x z))
                     (BF (BF y z))))
      Δ Δ Δ
      ΔΔ ΔΔ ΔΔ
      ΔΔΔ ΔΔΔ ΔΔΔ))
  (testing "fork-fork lemma"
    (are [w x y z] (= (BF (Δ (Δ w x) y) z)
                      (BF (BF z w) x))
      Δ Δ Δ Δ
      ΔΔ ΔΔ ΔΔ ΔΔ
      ΔΔΔ ΔΔΔ ΔΔΔ ΔΔΔ)))


(deftest Quote-tests
  (testing "quoting leaf"
    (is (= (Quote Δ) Δ)))
  (testing "quoting `MN`"
    (are [m n] (= (Quote (m n))
                  (Δ (Quote m) (Quote n)))
      Δ Δ
      K K

      K Δ
      Δ K

      Δ (K Δ) ;; swapped args application is not fully-reduced
      K (K Δ) ;; swapped args application is not fully-reduced

      (d Δ) Δ
      Δ (d Δ)

      (d (d Δ)) Δ
      Δ (d (d Δ))))
  (testing "failing, because application `m␣n` is not reduced"
    (are [m n] (not= (Quote (m n))
                     (Δ (Quote m) (Quote n)))
      (K Δ) Δ
      (K Δ) K
      (K Δ) (K Δ)
      D Δ
      Not False
      Not True)))


(deftest Quote-manual-tests
  (testing "one-node tree"
    (are [x y] (= x y)
      (Quote Δ) Δ))
  (testing "two-node trees"
    (are [x y] (= x y)
      (Quote K) (K Δ)))
  (testing "three-node trees"
    (are [x y] (= x y)
      (Quote (K Δ)) (Δ (K Δ) Δ)
      (Quote (Δ (Δ Δ))) (Δ Δ (K Δ))))
  (testing "four-node trees"
    (are [x y] (= x y)
      (Quote (Δ (Δ (Δ Δ)))) (K (K (K Δ)))
      (Quote (K K)) (Δ (K Δ) (K Δ))
      (Quote (Δ K Δ)) (Δ (K (K Δ)) Δ)
      (Quote (Δ (K Δ))) (K (Δ (K Δ) Δ))))
  (testing "five-node trees"
    (are [x y] (= x y)
      (Quote I) (Δ (K (K Δ)) (K Δ))
      (Quote (Δ (Δ K) Δ)) (Δ (K (K (K Δ))) Δ)
      (Quote (K (Δ (Δ Δ)))) (Δ (K Δ) (K (K Δ)))
      (Quote (Δ (Δ (Δ (Δ Δ))))) (K (K (K (K Δ))))
      (Quote (Δ (K Δ) Δ)) (Δ (K (Δ (K Δ) Δ)) Δ)
      (Quote (K (K Δ))) (Δ (K Δ) (Δ (K Δ) Δ))
      (Quote (Δ (Δ K Δ))) (K (Δ (K (K Δ)) Δ))
      (Quote (Δ (K K))) (K (Δ (K Δ) (K Δ))))))


;; Not confident that the following `Root` tests are correct and/or sufficient.

(deftest Root-Evaluation-tests
  (testing "leaf"
    (is (= Δ (Root Δ))))
  (testing "fork-leaf"
    (are [z] (= (Root (Δ Δ z))
                (Δ z))
      Δ
      ΔΔ
      ΔΔΔ
      (d Δ)))
  (testing "fork-stem"
    (are [y z] (= (Root (Δ (Quote (Δ y)) (Quote z)))
                  (Δ (Quote y) (Quote z)))
      Δ Δ
      ΔΔ ΔΔ
      ΔΔΔ ΔΔΔ
      (d Δ) (d Δ)))
  (testing "fork-fork-fork-leaf"
    (are [y z] (= (Root (Δ (Quote (Δ Δ y)) (Quote z)))
                  y)
      Δ Δ
      ΔΔ ΔΔ
      ΔΔΔ ΔΔΔ))
  (testing "fork-fork-fork-stem"
    (are [x y z] (= (Root (Δ (Δ (Quote (Δ x)) (Quote y)) (Quote z)))
                    ((y z) (x z)))
      K Δ Δ
      K K Δ
      K K K
      (K Δ) (K Δ) (K Δ)
      (K (K Δ)) (K (K Δ)) (K (K Δ))))
  (testing "fork-fork-fork-fork"
    (are [w x y z] (= (Root (Δ (Quote (Δ (Δ w x) y)) (Quote z)))
                      (z w x))
      Δ  Δ  Δ  Δ
      ΔΔ ΔΔ ΔΔ ΔΔ
      Δ Δ ΔΔΔ ΔΔΔ)))


(deftest Root-Branch-Evaluation-tests
  (testing "pass-through"
    (are [x] (= x (RB (Quote x)))
      Δ
      K
      (K Δ)
      (d Δ)))
  (testing "basic application"
    (are [x y z] (= (RB (Δ (Quote x) (Quote y))) z)
      Δ Δ K
      K Δ (K Δ)
      Δ K (Δ K)
      ΔΔΔ K Δ))
  (testing "semantic application"
    (are [x y] (= x y)
      True (RB (Δ (Quote Not) (Quote False)))
      False (RB (Δ (Quote Not) (Quote True))))))


(deftest Root-First-Evaluation-tests
  (testing "basic application"
    (are [x y] (= (x y) (RF x y))
      Δ Δ
      K Δ
      Δ K
      K K
      ΔΔΔ K))
  (testing "semantic application"
    (are [x y] (= x y)
      True (RF Not False)
      False (RF Not True))))


#_(run-test Root-Evaluation-tests)
#_(run-tests)

