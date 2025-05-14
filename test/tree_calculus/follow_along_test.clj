(ns tree-calculus.follow-along-test
  "Following along with Barry Jay's 'Reflective Programs in Tree Calculus'
  (2021) by replicating derivations, etc.

  To insert a unicode Delta:
  C-x 8, then ENTER, then type 0394"
  (:require
   [clojure.test :refer [are is deftest testing run-tests]]
   [tree-calculus.definitions :refer :all]
   [tree-calculus.utilities :refer :all]))


(deftest D-derivation-sequence ;; pg. 29
  (let [x Δ
        y x
        z x]
    (are [a b c d e f] (= a b c d e f)
      ((Δ ΔΔ ΔΔΔ) x y z)
      ((ΔΔΔ x) (Δ x) y z)
      ((Δ (Δ x)) y z)
      ((y z) (x z))
      (D x y z)
      (Δ Δ ΔΔ))))


(deftest S-derivation-sequence ;; pg. 29--30
  (let [x Anything-to-Not
        y I
        z False]
    (testing "`Sxyz` to `Dyxz` equivalence"
      (are [q] (= True q)
        (S x y z)
        (D y x z)))
    (testing "`Sxy` to `Dyx` equivalence"
      (are [a b c d] (= a b c d)
        (S x y)
        (D y x)
        (D y (K x y))
        ((D (K x) D) y)))
    (testing "`Sx` to `D(Kx)D` equivalence"
      (are [a b c d e] (= a b c d e)
        (S x)
        ((D (K x)) D)
        ((((K D) x) (K x)) ((K D) x))
        ((((D K) (K D)) x) ((K D) x))
        (((D (K D)) ((D K) (K D))) x)))))


(deftest S-check ;; pg. 30
  (let [x Anything-to-Not
        y I
        z False]
    (are [x] (= True x)
      (S x y z)
      (((d (K D)) ((d K) (K D))) x y z)
      ((((d K) (K D) x) ((K D) x)) y z)
      (((((K D) x) (K x)) ((K D) x)) y z)
      (((D (K x))) (D y) y z)
      (((D y) ((K x) y)) z)
      (D y x z)
      ((x z) (y z)))))


#_(run-tests)