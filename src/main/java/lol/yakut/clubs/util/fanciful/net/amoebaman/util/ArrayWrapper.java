package lol.yakut.clubs.util.fanciful.net.amoebaman.util;

import org.apache.commons.lang.Validate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public final class ArrayWrapper<E> {
   private E[] _array;

   public ArrayWrapper(E... elements) {
      this.setArray(elements);
   }

   public E[] getArray() {
      return this._array;
   }

   public void setArray(E[] array) {
      Validate.notNull(array, "The array must not be null.");
      this._array = array;
   }

   public boolean equals(Object other) {
      return !(other instanceof ArrayWrapper) ? false : Arrays.equals(this._array, ((ArrayWrapper)other)._array);
   }

   public int hashCode() {
      return Arrays.hashCode(this._array);
   }

   public static <T> T[] toArray(Iterable<? extends T> list, Class<T> c) {
      int size = -1;
      if (list instanceof Collection) {
         Collection coll = (Collection)list;
         size = coll.size();
      }

      if (size < 0) {
         size = 0;

         for(Iterator var7 = list.iterator(); var7.hasNext(); ++size) {
            T element = (T) var7.next();
         }
      }

      T[] result = (T[]) Array.newInstance(c, size);
      int i = 0;

      Object element;
      for(Iterator var5 = list.iterator(); var5.hasNext(); result[i++] = (T) element) {
         element = var5.next();
      }

      return result;
   }
}
