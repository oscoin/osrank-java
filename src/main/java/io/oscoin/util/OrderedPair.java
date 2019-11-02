package io.oscoin.util;

import java.io.Serializable;
import java.util.Comparator;

public class OrderedPair<E, F> implements Serializable {

    public E left;
    public F right;

    public OrderedPair(E left, F right)
    {
        this.left = left;
        this.right = right;
    }

    // Inherit
    public int hashCode()
    {
        // THIS CODE DERIVED FROM THE HASHCODE COMPUTATION FOR A LIST WITH 2 ELEMENTS FROM java.util.List
        int hashCode = 1;
        hashCode = 31*hashCode + (left==null ? 0 : left.hashCode());
        hashCode = 31*hashCode + (right==null ? 0 : right.hashCode());

        return hashCode;
    }

    // Inherit
    public boolean equals(Object object)
    {
        io.oscoin.util.OrderedPair casted = (io.oscoin.util.OrderedPair) object;
        if (casted.left.equals(left) && casted.right.equals(right))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static class OneSideOrderedPairComparator<E, F>
            implements Comparator<OrderedPair<E, F>>
    {
        private boolean left;

        public OneSideOrderedPairComparator(boolean left)
        {
            this.left = left;
        }

        public int compare(OrderedPair<E, F> one, OrderedPair<E, F> two)
        {
            if (true == left)
            {
                Comparable<Object> cOne = (Comparable<Object>) one.left;
                Comparable<Object> cTwo = (Comparable<Object>) two.left;

                return cOne.compareTo(cTwo);
            }
            else
            {
                Comparable<Object> cOne = (Comparable<Object>) one.right;
                Comparable<Object> cTwo = (Comparable<Object>) two.right;

                return cOne.compareTo(cTwo);
            }
        }
    }
}
