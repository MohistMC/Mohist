/*
 * Copyright (C) 2003, 2004 Jason Bevins (original libnoise code)
 * Copyright © 2010 Thomas J. Hodge (java port of libnoise)
 * 
 * This file is part of libnoiseforjava.
 * 
 * libnoiseforjava is a Java port of the C++ library libnoise, which may be found at 
 * http://libnoise.sourceforge.net/.  libnoise was developed by Jason Bevins, who may be 
 * contacted at jlbezigvins@gmzigail.com (for great email, take off every 'zig').
 * Porting to Java was done by Thomas Hodge, who may be contacted at
 * libnoisezagforjava@gzagmail.com (remove every 'zag').
 * 
 * libnoiseforjava is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * libnoiseforjava is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * libnoiseforjava.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package libnoiseforjava.module;

import libnoiseforjava.Interp;
import libnoiseforjava.Misc;
import libnoiseforjava.exception.ExceptionInvalidParam;

public class Terrace extends ModuleBase
{

   /// Noise module that maps the output value from a source module onto a
   /// terrace-forming curve.
   ///
   /// This noise module maps the output value from the source module onto a
   /// terrace-forming curve.  The start of this curve has a slope of zero;
   /// its slope then smoothly increases.  This curve also contains
   /// <i>control points</i> which resets the slope to zero at that point,
   /// producing a "terracing" effect.  Refer to the following illustration:
   ///
   /// @image html terrace.png
   ///
   /// To add a control point to this noise module, call the
   /// addControlPoint() method.
   ///
   /// An application must add a minimum of two control points to the curve.
   /// If this is not done, the getValue() method fails.  The control points
   /// can have any value, although no two control points can have the same
   /// value.  There is no limit to the number of control points that can be
   /// added to the curve.
   ///
   /// This noise module clamps the output value from the source module if
   /// that value is less than the value of the lowest control point or
   /// greater than the value of the highest control point.
   ///
   /// This noise module is often used to generate terrain features such as
   /// your stereotypical desert canyon.
   ///
   /// This noise module requires one source module.


   /// Number of control points stored in this noise module.
   int controlPointCount;

   /// Determines if the terrace-forming curve between all control points
   /// is inverted.
   boolean invertTerraces;

   /// Array that stores the control points.
   double [] controlPoints;


   public Terrace (ModuleBase sourceModule) throws ExceptionInvalidParam
   {
      super(1);
      setSourceModule(0, sourceModule);
      controlPointCount = 0;
      invertTerraces = false;
      controlPoints = new double [0];

   }

   /// Adds a control point to the terrace-forming curve.
   ///
   /// @param value The value of the control point to add.
   ///
   /// @pre No two control points have the same value.
   ///
   /// @throw ExceptionInvalidParam An invalid parameter was
   /// specified; see the preconditions for more information.
   ///
   /// Two or more control points define the terrace-forming curve.  The
   /// start of this curve has a slope of zero; its slope then smoothly
   /// increases.  At the control points, its slope resets to zero.
   ///
   /// It does not matter which order these points are added.
   public void addControlPoint (double value) throws ExceptionInvalidParam
   {
      // Find the insertion point for the new control point and insert the new
      // point at that position.  The control point array will remain sorted by
      // value.
      int insertionPos = findInsertionPos (value);
      insertAtPos (insertionPos, value);
   }


   /// Deletes all the control points on the terrace-forming curve.
   ///
   /// @post All control points on the terrace-forming curve are deleted.
   public void clearAllControlPoints ()
   {
      controlPoints = null;
      controlPointCount = 0;
   }

   /// Determines the array index in which to insert the control point
   /// into the internal control point array.
   ///
   /// @param value The value of the control point.
   ///
   /// @returns The array index in which to insert the control point.
   ///
   /// @pre No two control points have the same value.
   ///
   /// @throw ExceptionInvalidParam An invalid parameter was
   /// specified; see the preconditions for more information.
   ///
   /// By inserting the control point at the returned array index, this
   /// class ensures that the control point array is sorted by value.
   /// The code that maps a value onto the curve requires a sorted
   /// control point array.
   public int findInsertionPos (double value) throws ExceptionInvalidParam
   {
      int insertionPos;
      for (insertionPos = 0; insertionPos < controlPointCount; insertionPos++)
      {
         if (value < controlPoints[insertionPos])
            // We found the array index in which to insert the new control point.
            // Exit now.
            break;
         else if (value == controlPoints[insertionPos])
            // Each control point is required to contain a unique value, so throw
            // an exception.
            throw new ExceptionInvalidParam ("Invalid Parameter in Terrace Noise Moduled");        
      }
      return insertionPos;
   }

   public double getValue (double x, double y, double z)
   {
      assert (sourceModules[0] != null);
      assert (controlPointCount >= 2);

      // Get the output value from the source module.
      double sourceModuleValue = sourceModules[0].getValue (x, y, z);

      // Find the first element in the control point array that has a value
      // larger than the output value from the source module.
      int indexPos;
      for (indexPos = 0; indexPos < controlPointCount; indexPos++)
      {
         if (sourceModuleValue < controlPoints[indexPos])
            break;

      }

      // Find the two nearest control points so that we can map their values
      // onto a quadratic curve.
      int index0 = Misc.ClampValue (indexPos - 1, 0, controlPointCount - 1);
      int index1 = Misc.ClampValue (indexPos, 0, controlPointCount - 1);

      // If some control points are missing (which occurs if the output value from
      // the source module is greater than the largest value or less than the
      // smallest value of the control point array), get the value of the nearest
      // control point and exit now.
      if (index0 == index1)
         return controlPoints[index1];

      // Compute the alpha value used for linear interpolation.
      double value0 = controlPoints[index0];
      double value1 = controlPoints[index1];
      double alpha = (sourceModuleValue - value0) / (value1 - value0);
      if (invertTerraces)
      {
         alpha = 1.0 - alpha;
         double tempValue = value0;
         value0 = value1;
         value1 = tempValue;
      }

      // Squaring the alpha produces the terrace effect.
      alpha *= alpha;

      // Now perform the linear interpolation given the alpha value.
      return Interp.linearInterp (value0, value1, alpha);
   }

   /// Inserts the control point at the specified position in the
   /// internal control point array.
   ///
   /// @param insertionPos The zero-based array position in which to
   /// insert the control point.
   /// @param value The value of the control point.
   ///
   /// To make room for this new control point, this method reallocates
   /// the control point array and shifts all control points occurring
   /// after the insertion position up by one.
   ///
   /// Because the curve mapping algorithm in this noise module requires
   /// that all control points in the array be sorted by value, the new
   /// control point should be inserted at the position in which the
   /// order is still preserved.
   public void insertAtPos (int insertionPos, double value)
   {
      // Make room for the new control point at the specified position within
      // the control point array.  The position is determined by the value of
      // the control point; the control points must be sorted by value within
      // that array.
      double[] newControlPoints = new double[controlPointCount + 1];

      for (int i = 0; i < controlPointCount; i++)
      {
         if (i < insertionPos)
            newControlPoints[i] = controlPoints[i];
         else
            newControlPoints[i + 1] = controlPoints[i];  
      }

      controlPoints = newControlPoints;
      ++controlPointCount;

      // Now that we've made room for the new control point within the array,
      // add the new control point.
      controlPoints[insertionPos] = value;
   }

   /// Creates a number of equally-spaced control points that range from
   /// -1 to +1.
   ///
   /// @param controlPointCount The number of control points to generate.
   ///
   /// @pre The number of control points must be greater than or equal to
   /// 2.
   ///
   /// @post The previous control points on the terrace-forming curve are
   /// deleted.
   ///
   /// @throw ExceptionInvalidParam An invalid parameter was
   /// specified; see the preconditions for more information.
   ///
   /// Two or more control points define the terrace-forming curve.  The
   /// start of this curve has a slope of zero; its slope then smoothly
   /// increases.  At the control points, its slope resets to zero.
   void makeControlPoints (int controlPointCount) throws ExceptionInvalidParam
   {
      if (controlPointCount < 2)
         throw new ExceptionInvalidParam ("Invalid Parameter in Terrace Noise Module");

      clearAllControlPoints ();

      double terraceStep = 2.0 / ((double)controlPointCount - 1.0);
      double curValue = -1.0;
      for (int i = 0; i < (int)controlPointCount; i++)
      {
         addControlPoint (curValue);
         curValue += terraceStep;
      }
   }

   /// Returns a pointer to the array of control points on the
   /// terrace-forming curve.
   ///
   /// @returns A pointer to the array of control points in this noise
   /// module.
   ///
   /// Two or more control points define the terrace-forming curve.  The
   /// start of this curve has a slope of zero; its slope then smoothly
   /// increases.  At the control points, its slope resets to zero.
   ///
   /// Before calling this method, call getControlPointCount() to
   /// determine the number of control points in this array.
   ///
   /// It is recommended that an application does not store this pointer
   /// for later use since the pointer to the array may change if the
   /// application calls another method of this object.
   public double[] getControlPointArray ()
   {
      return controlPoints;
   }

   /// Returns the number of control points on the terrace-forming curve.
   ///
   /// @returns The number of control points on the terrace-forming
   /// curve.
   public int getControlPointCount ()
   {
      return controlPointCount;
   }

   /// Enables or disables the inversion of the terrace-forming curve
   /// between the control points.
   ///
   /// @param invert Specifies whether to invert the curve between the
   /// control points.
   public void invertTerraces (boolean invert)
   {
      if (invert)
         invertTerraces = invert;
   }

   /// Determines if the terrace-forming curve between the control
   /// points is inverted.
   ///
   /// @returns
   /// - @a true if the curve between the control points is inverted.
   /// - @a false if the curve between the control points is not
   ///   inverted.
   public boolean isTerracesInverted ()
   {
      return invertTerraces;
   }

}
