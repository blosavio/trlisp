(ns tree-calculus.exercising-test
  "Tests that go beyond the basic definitions, perhaps attempting to be
  exhaustive.

  Page numbers reference BL paper journal.

  To insert a unicode Delta:
  C-x 8, then ENTER, then type 0394"
  (:require
   [clojure.test :refer [are is deftest testing run-tests]]
   [tree-calculus.definitions :refer :all]
   [tree-calculus.utilities :refer :all]))


(deftest implicit-application-with-thingys
  (are [x y] (= x y)
    (Δ Δ)
    ((Δ) (Δ))

    (Δ Δ Δ)
    ((Δ (Δ)) (Δ))

    Δ
    ((Δ (Δ) (Δ)) (Δ))

    (Δ Δ Δ)
    (Δ (Δ) (Δ))

    Δ
    (Δ (Δ) (Δ) (Δ))))


(deftest page-11
  (are [a b] (= a b)
    ΔΔΔ (K ΔΔΔ Δ)
    (K ΔΔ) (I (K ΔΔ))
    ΔΔ (D ΔΔ ΔΔ ΔΔ)))


(deftest page-12
  (testing "x = y = z"
    (are [a b] (= a b)
      (Δ Δ ΔΔ) (D Δ Δ Δ)
      ΔΔ (D ΔΔ ΔΔ ΔΔ)
      ΔΔ (D ΔΔΔ ΔΔΔ ΔΔΔ)))
  (testing "x ≠ y ≠ z"
    (are [a b] (= a b)
      Δ (D ΔΔΔ ΔΔ Δ))))


(deftest page-13
  (testing "attempting to reconcile B. Jay and J. Bader")
  (are [a b] (= a b)
    Δ (I Δ)))


(deftest page-14
  (testing "`S` operator"
    (let [x ΔΔΔ
          y ΔΔ
          z Δ]
      (are [q] (= q (Δ ΔΔΔ))
        (S x y z)
        (D y x z)))))


;; page 16 `Zero?` predicate

;; pages 17--18 verify logical `And`, which required swapping out Bader's
;; formulation

;; page 19 logical `Or`
;; page 20 logical `Not`
;; page 21 logical `Imp`
;; page 22 logical `Iff`
;; page 23 `Pair`, `First`, and `Second`


(deftest K-extended-tests
  (testing "abstract leafs"
    (are [x y] (= x y)
      :a (K :a :b)
      (Δ :a :b) (K (Δ :a :b) (Δ :c :d)))))


(deftest I-extended-tests
  (testing "abstract leafs"
    (are [x y] (= x y)
      :a (I :a)
      (Δ :a) (I (Δ :a))
      (Δ :a :b) (I (Δ :a :b)))))


(deftest D-extended-tests
  (testing "other patterns"
    (are [x y] (= x y)
      (Δ Δ (Δ Δ)) (D Δ Δ Δ))))


;; verifying manual evaluation of `plus` from pages 68--69 of B. Losavio notes

(def f-plus (fn [m]
              (fn [p]
                ((Δ m I) (K (fn [x]
                              (fn [n] (K (p x n)))))))))

(def f (Swap f-plus))

(def ⨁ ((d (Wait-1 Self-Apply)) (K f)))

(def ⊙ ((d (K ⨁)) (K Self-Apply)))

(def ⋈ (((d I) (d (K ⨁))) (K Self-Apply)))

(def One (nat->tree 1))

(def Two (nat->tree 2))


(deftest plus-extended-tests
  (are [x] (= (nat->tree 3) x)
    (Plus One Two)
    ((Y f-plus) One Two)
    ((Z (Swap f-plus)) One Two)
    ((Wait Self-Apply ⨁) One Two)
    ((d I) ⊙ One Two)
    ((⊙ One) (I One) Two)
    (((d (K ⨁)) (K Self-Apply) One) One Two)
    ((K Self-Apply One) (K ⨁ One) One Two)
    ((Self-Apply ⨁) One (K (K Δ)))
    ((⨁ ⨁) One Two)
    (((d (Wait-1 Self-Apply)) (K f) ⨁) One Two)
    ((K f ⨁) ((Wait-1 Self-Apply) ⨁) One Two)
    (f ⋈ One Two)
    ((Swap f-plus) ⋈ One Two)
    (((f-plus One) ⋈) Two)
    ((Δ One I) (K (fn [x] (fn [n] (K (⋈ x n))))) Two)
    (((I (K (fn [x] (fn [n] (K (⋈ x n)))) Δ)) Δ) (K (K Δ)))
    (K (⋈ Δ (K (K Δ))))
    (K (((d I) (d (K ⨁))) (K Self-Apply) Δ (K (K Δ))))
    (K (((d (K ⨁)) (K Self-Apply)) Δ (I Δ) (K (K Δ))))
    (K ((K Self-Apply Δ) (K ⨁ Δ) Δ (K (K Δ))))
    (K ((Self-Apply ⨁) Δ (K (K Δ))))
    (K ((⨁ ⨁) Δ (K (K Δ))))
    (K ((d (Wait-1 Self-Apply)) (K f) ⨁ Δ (K (K Δ))))
    (K ((K f ⨁) ((Wait-1 Self-Apply) ⨁) Δ (K (K Δ))))
    (K (f (⨁ ⨁) Δ (K (K Δ))))
    (K ((Swap f-plus) (⨁ ⨁) Δ (K (K Δ))))
    (K (((f-plus Δ) (⨁ ⨁)) (K (K Δ))))
    (K ((Δ Δ) I (K (fn [x] (fn [n] (K (⨁ ⨁) x n)))) (K (K Δ))))
    (K (I (K (K Δ))))
    (K (K (K Δ)))))


;; verify sidebar on page 69 or B. Losavio notes

(def ♦ (K (d I)))

(def ★ ((d K) (K Δ)))

(def ☒ ((d ★) (K Δ)))


(deftest plus-sidebar-tests
  (are [x] (= x ((Wait-1 Self-Apply) ⨁))
    (((d ((d (K (K Self-Apply))) ☒)) ♦) ⨁)
    ((♦ ⨁) (((d (K (K Self-Apply))) ☒) ⨁))
    (((K (d I)) ⨁) ((☒ ⨁) ((K (K Self-Apply)) ⨁)))
    ((d I) (((d ★) (K Δ) ⨁) (K Self-Apply)))
    ((d I) ((K Δ ⨁) (★ ⨁) (K Self-Apply)))
    ((d I) (Δ ((d K) (K Δ) ⨁) (K Self-Apply)))
    ((d I) ((Δ ((K Δ ⨁) (K ⨁))) (K Self-Apply)))
    ((d I) ((Δ (Δ (K ⨁))) (K Self-Apply)))
    ((d I) ((d (K ⨁)) (K Self-Apply)))))


;; Page 61 of the pdf and lines 648--662 of the coq file set `r` to `I`, but
;; that incorrectly returns a full copy of arg `x`, not the slice of `x` that
;; corresponds to `s` in pattern `p`.

#_(deftest pattern-matching-finale
    (are [a b] (= a b)
      K (let [f Δ]
          ((Extension (Y f) f I)
           (Y K)))))


#_(ns-unmap *ns* 'pattern-matching-finale)


(deftest pattern-matching-version-2-tests
  (testing "pattern: leaf, arg: leaf"
    (are [p s r x] (= ((Extension-2 p s r) x)
                      s)
      Δ Δ :ignored Δ))
  (testing "pattern: leaf; arg: stem"
    (are [s r u] (= ((Extension-2 Δ s r) (Δ u))
                    (r (Δ u)))
      :ignored Δ Δ
      :ignored Δ ΔΔ
      :ignored Δ ΔΔΔ))
  (testing "pattern: leaf; arg: fork"
    (are [s r t u] (= ((Extension-2 Δ s r) (Δ t u))
                      (r (Δ t u)))
      :ignored Δ Δ Δ
      :ignored Δ ΔΔ ΔΔ
      :ignored Δ ΔΔΔ ΔΔΔ))
  (testing "pattern: stem; arg: leaf"
    (are [p s r] (= ((Extension-2 (Δ p) s r) Δ)
                    (r Δ))
      Δ :ignored Δ
      ΔΔ  :ignored ΔΔ
      ΔΔΔ :ignored ΔΔΔ))
  (testing "pattern: stem; arg: stem"
    (are [p s r u] (= ((Extension-2 (Δ p) s r) (Δ u))
                      ((Extension-2 p s (Δ K (K r))) u))
      Δ ΔΔΔ ΔΔΔ ΔΔΔ
      ΔΔ Δ Δ Δ
      ΔΔΔ Δ Δ Δ

      ;; The following unit fails with `Extension-2`, but not `Extension`.
      ;;(Δ (Δ ΔΔ ΔΔ) (Δ ΔΔ ΔΔ)) ΔΔ I (Δ (Δ ΔΔΔ ΔΔΔ) (Δ ΔΔΔ ΔΔΔ))
      Δ ΔΔ ΔΔΔ (Δ (Δ ΔΔΔ ΔΔΔ) (Δ ΔΔΔ ΔΔΔ))))
  (testing "pattern: stem; arg: fork"
    (are [p s r t u] (= ((Extension-2 (Δ p) s r) (Δ t u))
                        (r (Δ t u)))
      Δ   :ignored Δ   Δ    Δ
      ΔΔ  :ignored ΔΔ  ΔΔ  ΔΔ
      ΔΔΔ :ignored ΔΔΔ ΔΔΔ ΔΔΔ))
  (testing "pattern: fork; arg: leaf"
    (are [p q s r] (= ((Extension-2 (Δ p q) s r) Δ)
                      (r Δ))
      Δ   Δ   :ignored Δ
      ΔΔ  ΔΔ  :ignored ΔΔ
      ΔΔΔ ΔΔΔ :ignored ΔΔΔ))
  (testing "pattern: fork; arg: stem"
    (are [p q s r u] (= ((Extension-2 (Δ p q) s r) (Δ u))
                        (r (Δ u)))
      Δ   Δ   :ignored Δ   Δ
      ΔΔ  ΔΔ  :ignored ΔΔ  ΔΔ
      ΔΔΔ ΔΔΔ :ignored ΔΔΔ ΔΔΔ))
  (testing "pattern: fork; arg: fork"
    (are [p q s r t u] (= ((Extension-2 (Δ p q) s r) (Δ t u))
                          ((Extension-2 p
                                        (Extension-2 q
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


(deftest bf-extended-tests
  (let [z Δ]
    (are [x] (= x (BF I z))
      (BF I z)
      (((On-Fork (Triage BF-Leaf (BF-Stem Eager) BF-Fork)) I) BF z)
      ((((Triage BF-Leaf (BF-Stem Eager) BF-Fork) K) K) BF z)
      ((((BF-Stem Eager) Δ) K) BF z)
      ((Eager (BF Δ z)) (BF (BF K z)))
      ((Eager (Δ z)) (BF (BF K z)))
      (BF (BF K z) (Δ z))
      (BF (K z) (Δ z))
      (((On-Fork (Triage BF-Leaf (BF-Stem Eager) BF-Fork)) (K z)) BF (Δ z))
      ((((Triage BF-Leaf (BF-Stem Eager) BF-Fork) Δ) z) BF (Δ z))
      ((BF-Leaf z) BF (Δ z))
      z)))


#_(run-tests)

