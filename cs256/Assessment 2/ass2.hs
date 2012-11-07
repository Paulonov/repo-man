
-- CS256 Assessment 2
-- Paul Roper
-- University Number: 1121480

-- Exercise 1
data List a = Empty | Cons a (List a)

hd :: List a -> a
hd Empty = error "The empty list has no head"
hd (Cons x _) = x

tl :: List a -> List a
tl Empty = error "The empty list has no tail"
tl (Cons _ l) = l

myshow :: Show a => List a -> String
myshow l = '[' : (f l)
	where
		f :: Show a => List a -> String
		f Empty = "]"
		f (Cons x Empty) = (show x) ++ "]"
		f (Cons x l) = (show x) ++ "," ++ (f l)

-- A pointer in C can point to the first element of a list
-- Haskell does similar thing with hd

-- Struct list {
--     Pointer to data
--     Pointer to next element   
-- } 

-- Exercise 2
dec :: Int -> String
dec x = show x

bin :: Int -> String
bin x = 

--

-- Exercise 3

--

-- Exercise 4

--

--
