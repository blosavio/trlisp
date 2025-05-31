(ns tree-calculus.definitions
  "Exploring Tree Calculus discovered by Professor Barry Jay.

  See _Reflective Programs in Tree Calculus_
  ([2001](https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf))
  and Johannes Bader's [Tree Calculus website](https://treecalcul.us/).

  This [Hacker News post](https://news.ycombinator.com/item?id=42373437)
  has many useful comments, as is Timur Latypoff's
  [visualization](https://latypoff.com/tree-calculus-visualized/)."
  (:require [tree-calculus.definers :refer :all]
            [thingy.core :refer [assign-thingy-fn!
                                 make-thingy]]))


(load-file "src/tree_calculus/docstrings.clj")


(Defn length-is? [n] #(= n (count %)))


(Def leaf? (length-is? 0))
(Def stem? (length-is? 1))
(Def fork? (length-is? 2))


(defn Apply-Sentinel
  {:UUIDv4 #uuid "88a2dfed-f333-46d8-a671-1fcdec34edc3"
   :no-doc true}
  [val]
  (throw (Exception. (str *ns* "/Apply: Non-tree in the left child node of the
 first argument. This value should be Δ or some derivative. Actual value: " val))))


(defn Apply
  "Apply tree left-hand tree `L` to right-hand tree `R`. `L` and `R` are
  \"alternate function invoke\" vectors, which, when at the head of an evaluated
  S-expression, invoke a custom function.

  Following reductions listed at Barry Jay's [Reflective Programs in Tree
  Calculus](https://github.com/barry-jay-personal/tree-calculus/blob/master/tree_book.pdf)
  (2021, pages 5 and 28).

  Rules:

  * Rule 0a: `Δ      ␣b → Δb`
  * Rule 0b: `Δa     ␣b → Δab`
  * Rule 1:  `ΔΔ    y␣z → y`           *Kernel Rule*, or *K-Rule*
  * Rule 2:  `Δ(Δx) y␣z → (y␣z)␣(x␣z)` *Stem Rule*, or *S-Rule*
  * Rule 3:  `Δ(Δwx)y␣z → (z␣w)␣x`     *Fork Rule*, or *F-Rule*

  Δ is the tree operator, which represents a node in an unlabelled, binary
  tree.

  `a`, `b`, `w`, `x`, `y`, `z` are arbitrary sub-trees. The letters convey no
  semantics, but are chosen to help track of their original position.

  `␣` is the _space operator_, which represents application of one tree to
  another.

  Note the structural progression. Rules 0a and 0b concern applying a plain leaf
  and plain stem, respectively. Rules 1, 2, and 3 concern applying a fork whose
  left value is a plain leaf, plain stem, or fork, respectively."
  {:UUIDv4 #uuid "b8a2e67f-7a80-451a-9462-8b0dd4349b62"
   :implementation-notes ["The definitions of some tree calculus operators, e.g.,
  `plus`, contain an anonymous function, that requires handling before
  dispatching on node kind."

                          "The recursive calls to `Apply` are not strictly
  necessary. The explicit calls avoid delegation to the `.invoke()` method,
  which ultimately passes back to `Apply`."]}
  [L R]
  (cond
    (fn? L) (L R)
    (leaf? L) (make-thingy R)
    (stem? L) (make-thingy (first L) R)
    (fork? L) (cond
                (fn? (first L)) (Apply-Sentinel (first L))
                (leaf? (first L)) (second L)
                (stem? (first L)) (Apply (Apply (second L) R)
                                         (Apply (ffirst L) R))
                (fork? (first L)) (Apply (Apply R (ffirst L))
                                         (second (first L))))))


(defn Mult-Apply
  "For all thingys, applies `Apply` until `args` exhausted."
  {:UUIDv4 #uuid "4912473b-de66-4f77-b786-5d31fec37091"}
  [& args]
  (reduce Apply args))


(assign-thingy-fn! Mult-Apply)


(def Δ (make-thingy))


;; combinators


(Def K (Δ Δ))


(Def I (Δ (Δ Δ)
          (Δ Δ)))


(Def D (Δ (Δ Δ)
          (Δ Δ Δ)))


(Defn d [x] (Δ (Δ x)))


(Def S ((d (K D))
        ((d K) (K D))))


(Def B (S (K S) K))


(Def C (S (S (K (S (K S) K)) S) (K K)))


(Def W (S S (S K)))


(Def True K)


(Def False (K I))


(Def And (d (K (K I))))


(Def Or ((d (K K)) I))


(Def Implies (d (K K)))


(Def Not ((d (K K))
          ((d (K (K I))) I)))


(Def Iff (Δ (Δ I Not) Δ))


(Def Zero? ((d (K (K (K (K I)))))
            ((d (K K)) Δ)))


(Defn Pair [x y] (Δ x y))


(Defn First [pair] (((Δ pair) Δ) K))


(Defn Second [pair] (((Δ pair) Δ) (K I)))


(Def Successor K)


(Def Predecessor ((d (K (K I)))
                  ((d (K Δ)) Δ)))


(Defn Query [is0 is1 is2] ((d (K is1))
                           ((d (K (K I)))
                            ((d (K (K (K (K (K is2))))))
                             ((d (K (K (K is0))))
                              Δ)))))


(Def Leaf? (Query True False False))
(Def Stem? (Query False True False))
(Def Fork? (Query False False True))


(Defn Wait [x y] ((d I)
                  ((d (K y)) (K x))))


(Defn Wait-1 [x] ((d
                   ((d (K (K x)))
                    ((d
                      ((d K)
                       (K Δ)))
                     (K Δ))))
                  (K (d I))))


(Defn Wait-2 [x y] ((d
                     (d (K ((d (K y))
                            (K x)))))
                    (((d ((d K)
                          (K Δ))) (K Δ))
                     (K (d I)))))


(Def Self-Apply ((d I) I))


(Defn Z [f] (Wait Self-Apply
                  ((d (Wait-1 Self-Apply)) (K f))))


(Defn Swap [f] ((d (K f)) ((d ((d K) (K Δ))) (K Δ))))


(Defn Y [f] (Z (Swap f)))


(Def Plus
  (Y (fn [m]
       (fn [plus]
         ((Δ m I) (K (fn [x]
                       (fn [n] (Successor (plus x n))))))))))


(Def Minus
  (Y (fn [m]
       (fn [minus]
         (fn [n]
           ((Δ n m) (K (minus (Predecessor m)))))))))


(Def Times
  (Y (fn [m]
       (fn [times]
         (fn [n]
           ((Δ n Δ) (K (fn [x] ((Plus m) ((times m) x))))))))))


(Def Divide
  (fn [m' n']
    ((Y
      (fn [m]
        (fn [divide]
          (fn [n]
            (fn [i]
              ((Δ (Minus m (Times i n)) i)
               (fn [_] (K (divide m n (Successor i)))))))))) m' n' (K Δ))))


(defn List
  "Returns a tree calculus list containing elements of `args`"
  {:UUIDv4 #uuid "36cff714-0362-4c88-b230-a6c94e05a5a4"}
  [& args]
  (reduce #(Δ %2 %1) Δ (reverse args)))


(defn Bite
  "Given eight tree calculus bits `b0` to `b7`, returns a tree calculus byte.

  Note: Convert Clojure/Java bits with `(map bit->Bit bits)`"
  {:UUIDv4 #uuid "e463faad-d262-4679-9c30-effcc8bc67d2"}
  [b0 b1 b2 b3 b4 b5 b6 b7]
  (List b0 b1 b2 b3 b4 b5 b6 b7))


(Def T-Nil Δ)


(Defn T-Cons [h t] (Δ h t))


(Def T-Head (fn [xs] (((Δ xs) (K I)) K)))


(Def T-Tail (fn [xs] (((Δ xs) (K I)) (K I))))


(Def List-Map-Swap
  (fn [x] (Δ x
             (K (K T-Nil))
             (fn [h]
               (fn [t]
                 (fn [m]
                   (fn [f]
                     (T-Cons (f h) (m t f)))))))))


(Def List-Map (Swap (Y List-Map-Swap)))


(Def List-FoldLeftAux (fn [y] (Δ y (K (K I))
                                 (fn [h]
                                   (fn [t]
                                     (fn [lfold]
                                       (fn [f]
                                         (fn [x]
                                           (lfold t f (f x h))))))))))


(Defn List-FoldLeft [f x y] ((Y List-FoldLeftAux) y f x))


(Def List-FoldRightAux (fn [y] (Δ y (K (K I))
                                  (fn [h]
                                    (fn [t]
                                      (fn [rfold]
                                        (fn [f]
                                          (fn [x]
                                            (f h (rfold t f x))))))))))


(Defn List-FoldRight [f x y] ((Y List-FoldRightAux) y f x))


(Defn List-Append [xs ys] (List-FoldRight (fn [h t] (T-Cons h t))
                                          ys
                                          xs))


(Defn List-Reverse [z] (List-FoldLeft (fn [x y] (T-Cons y x)) T-Nil z))


(Def Size (Y (fn [x] ((Stem? x)
                      (fn [s] (Δ
                               (x Δ)
                               Δ
                               (fn [x1] (K (Successor (s x1))))))
                      (Δ
                       x
                       (K (Successor Δ))
                       (fn [x1]
                         (fn [x2]
                           (fn [s]
                             (Successor
                              (Plus (s x1) (s x2)))))))))))


(Def Equal? (Y (fn [x] ((Stem? x)
                        (fn [e]
                          (fn [y]
                            ((Stem? y)
                             (e (Δ (x Δ) Δ K)
                                (Δ (y Δ) Δ K))
                             (K I))))
                        (Δ
                         x
                         (fn [e] (fn [y] (Leaf? y)))
                         (fn [x1]
                           (fn [x2]
                             (fn [e]
                               (fn [y]
                                 ((Fork? y)
                                  (Δ
                                   y
                                   Δ
                                   (fn [y1]
                                     (fn [y2]
                                       (e x1 y1 (e x2 y2) (K I)))))
                                  (K I)))))))))))


(Defn Tag [t f] ((d t) ((d f) (K K))))


(Def Get-Tag (fn [p] (First ((First p) Δ))))


(Def Un-Tag (fn [x] (First ((First (Second x)) Δ))))


(Defn Tag-Wait [t] (fn [w] (Tag t (Wait Self-Apply w))))


(Defn Y-t [t f] (Tag t (Wait Self-Apply ((d (Tag-Wait t)) (K (Swap f))))))


(Def error Δ)


(Defn Type-Check [x u] ((Fork? x)
                        (Δ x Δ
                           (fn [t]
                             (fn [v]
                               (Equal? u v t error))))
                        error))


(Defn Typed-App [f x] (Tag (Type-Check (Get-Tag f)
                                       (Get-Tag x))
                           ((Un-Tag f)
                            (Un-Tag x))))


(Def Stem?-2 (fn [z] (Δ z Δ (K (K Δ)))))


(Def Fork?-2 (fn [z] (Δ z (K K) (K (K Δ)))))


(Defn Triage [f0 f1 f2]
  (fn [a] ((Stem? a)
           (Δ (a Δ) Δ (fn [x] (K (f1 x))))
           (Δ a f0 f2))))


(Def Size-Variant (Y (Triage (K (K Δ))
                             (fn [y]
                               (fn [s] (K (s y))))
                             (fn [y]
                               (fn [z]
                                 (fn [s] (K (Plus (s y) (s z)))))))))


(Def Equal?-Variant
  (Y (Triage
      (fn [e]
        (Triage True
                (K False)
                (K (K False))))
      (fn [y]
        (fn [e]
          (Triage False
                  (e y)
                  (K (K False)))))
      (fn [y1]
        (fn [y2]
          (fn [e]
            (Triage False
                    (K False)
                    (fn [z1]
                      (fn [z2]
                        (e y1 z1 (e y2 z2) False))))))))))


(Defn Leaf-Case [s] (fn [r]
                      (fn [x]
                        ((Leaf? x) s (r x)))))


(Defn Stem-Case [f] (fn [r]
                      (fn [x] ((Stem? x)
                               (Δ (x Δ) Δ
                                  (fn [y]
                                    (K ((f
                                         (fn [z]
                                           (r (Δ z))))
                                        y))))
                               (r x)))))


(Def Fork-Case-Aux-1 (fn [x1]
                       (fn [x2]
                         (fn [r1]
                           (r1 (Δ x1 x2))))))


(Defn Fork-Case-Aux-2 [p1] (fn [x2]
                             (fn [r2]
                               (r2 (Δ p1 x2)))))


(Defn Fork-Case [f] (fn [r]
                      (fn [x]
                        ((Fork? x)
                         (Δ x Δ
                            (fn [x1]
                              (fn [x2]
                                ((Wait f Fork-Case-Aux-1)
                                 x1 x2 r))))
                         (r x)))))


(Defn Tree-Case [p s]
  (fn [r]
    (fn [x]
      (((cond
          (= p x) (K (fn [_] s))
          (leaf? p) (Leaf-Case s)
          (stem? p) (Stem-Case (Tree-Case (First p) s))
          (fork? p) (Fork-Case
                     (Tree-Case (First p)
                                (Wait (Tree-Case (Second p)
                                                 (K s))
                                      (Fork-Case-Aux-2 (First p))))))
        r) x))))


(Defn Extension [p s r] (Wait (Tree-Case p s) r))


(Defn Tree-Case-2 [p s]
  (fn [r]
    (fn [x]
      ((((Equal? p x)
         (K (fn [_] s))
         ((Leaf? p)
          (Leaf-Case s)
          ((Stem? p)
           (Stem-Case (Tree-Case (First p) s))
           (Fork-Case
            (Tree-Case (First p)
                       (Wait (Tree-Case (Second p)
                                        (K s))
                             (Fork-Case-Aux-2 (First p))))))))
        r) x))))


(Defn Extension-2 [p s r] (Wait (Tree-Case-2 p s) r))


(Def Eager (fn [z]
             (fn [f]
               (Δ z I (K (K I)) I f z))))


(Defn Db [x] ((d ((d (K x)) I)) (K D)))


(Def BF-Leaf (fn [y] (K (K y))))


(Defn BF-Stem [e] (fn [x]
                    (fn [y]
                      ((d ((d (K (K e))) (Db x)))
                       ((d ((d K) (Db y))) (K D))))))


(Def BF-Fork (fn [w]
               (fn [x]
                 (K ((d
                      ((d K)
                       ((d (d (K w)))
                        (K D))))
                     (K (d (K x))))))))


(Defn On-Fork [f] (fn [x] (Δ (Fork?-2 x) (Δ x Δ f) (K (K (K x))))))


(Def BF (Y
         (On-Fork
          (Triage BF-Leaf
                  (BF-Stem Eager)
                  BF-Fork))))


(Def Quote-Aux
  (fn [x]
    ((Stem? x)
     (fn [q]
       (Δ (x Δ) Δ (fn [x1]
                    (K (K (q x1))))))
     (Δ x (K Δ) (fn [x1]
                  (fn [x2]
                    (fn [q]
                      (Δ (K (q x1)) (q x2)))))))))


(Def Quote (Y Quote-Aux))


(Def Root-L (fn [r]
              (fn [y]
                (fn [z]
                  (r y)))))


(Def Root-S (fn [x]
              (fn [r]
                (fn [y]
                  (fn [z]
                    (r (Δ (Δ y z) (Δ x z))))))))


(Def Root-F (fn [w]
              (fn [x]
                (fn [r]
                  (fn [y]
                    (fn [z]
                      (r (Δ (Δ z w) x))))))))


(Def Root-N (fn [t]
              (fn [y]
                (fn [r]
                  ((((Triage Root-L
                             Root-S
                             Root-F)
                     (r t)) r) y)))))


(Def Root-Aux (fn [a]
                (fn [r]
                  (Δ a Δ (fn [f]
                           (((On-Fork Root-N) (r f)) r))))))


(Def Root (Y Root-Aux))


(Def RB-Aux (fn [x]
              (fn [r]
                ((Triage Δ
                         (fn [y] (Δ (r y)))
                         (fn [y]
                           (fn [z]
                             (Δ (r y) (r z)))))
                 (Root x)))))


(Def RB (Y RB-Aux))


(Defn RF [f z] (RB (Δ (Quote f) (Quote z))))

