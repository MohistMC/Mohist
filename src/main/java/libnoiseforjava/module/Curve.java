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

public class Curve extends ModuleBase
{
   /// Noise module that maps the output value from a source module onto an
   /// arbitrary function curve.
   ///
   /// This noise module maps the output value from the source module onto an
   /// application-defined curve.  This curve is defined by a number of
   /// <i>control points</i>; each control point has an <i>input value</i>
   /// that maps to an <i>output value</i>.
   ///
   /// To add the control points to this curve, call the addControlPoint()
   /// method.  Note that the class ControlPoint follows the class Curve in
   /// this file.
   ///
   /// Since this curve is a cubic spline, an application must add a minimum
   /// of four control points to the curve.  If this is not done, the
   /// getValue() method fails.  Each control point can have any input and
   /// output value, although no two control points can have the same input
   /// value.  There is no limit to the number of control points that can be
   /// added to the curve.  
   ///
   /// This noise module requires one source module

   int controlPointCount;
   ControlPoint[] controlPoints;

   public Curve (ModuleBase sourceModule) throws ExceptionInvalidParam
   {
      super(1);
      setSourceModule(0, sourceModule);
      controlPointCount = 0;
      controlPoints= new ControlPoint[1];
      controlPoints[0] = new ControlPoint(0.0, 0.0);
   }

   public void addControlPoint (double inputValue, double outputValue)
      throws ExceptionInvalidParam
   {
      // Find the insertion point for the new control point and insert the new
      // point at that position.  The control point array will remain sorted by
      // input value.
      int insertionPos = findInsertionPos(inputValue);
      insertAtPos (insertionPos, inputValue, outputValue);
   }

   public void clearAllControlPoints ()
   {
      controlPoints = null;
      controlPointCount = 0;
   }

   public int findInsertionPos (double inputValue) throws ExceptionInvalidParam
   {
      int insertionPos;
      for (insertionPos = 0; insertionPos < controlPointCount; insertionPos++)
      {
         if (inputValue < controlPoints[insertionPos].inputValue)
            // We found the array index in which to insert the new control point.
            // Exit now.
            break;
         else if (inputValue == controlPoints[insertionPos].inputValue)
            // Each control point is required to contain a unique input value, so
            // throw an exception.
            throw new ExceptionInvalidParam("Invalid Parameter in Curve");
      }
      return insertionPos;
   }

   public double getValue (double x, double y, double z)
   {
      assert (sourceModules[0] != null);
      assert (controlPointCount >= 4);

      // Get the output value from the source module.
      double sourceModuleValue = sourceModules[0].getValue (x, y, z);

      // Find the first element in the control point array that has an input value
      // larger than the output value from the source module.
      int indexPos;
      for (indexPos = 0; indexPos < controlPointCount; indexPos++)
      {
         if (sourceModuleValue < controlPoints[indexPos].inputValue)
            break;

      }

      // Find the four nearest control points so that we can perform cubic
      // interpolation.
      int index0 = Misc.ClampValue (indexPos - 2, 0, controlPointCount - 1);
      int index1 = Misc.ClampValue (indexPos - 1, 0, controlPointCount - 1);
      int index2 = Misc.ClampValue (indexPos    , 0, controlPointCount - 1);
      int index3 = Misc.ClampValue (indexPos + 1, 0, controlPointCount - 1);

      // If some control points are missing (which occurs if the value from the
      // source module is greater than the largest input value or less than the
      // smallest input value of the control point array), get the corresponding
      // output value of the nearest control point and exit now.
      if (index1 == index2) {
         return controlPoints[index1].outputValue;
      }

      // Compute the alpha value used for cubic interpolation.
      double input0 = controlPoints[index1].inputValue;
      double input1 = controlPoints[index2].inputValue;
      double alpha = (sourceModuleValue - input0) / (input1 - input0);

      // Now perform the cubic interpolation given the alpha value.
      return Interp.cubicInterp(
            controlPoints[index0].outputValue,
            controlPoints[index1].outputValue,
            controlPoints[index2].outputValue,
            controlPoints[index3].outputValue,
            alpha);
   }

   public void insertAtPos (int insertionPos, double inputValue,
         double outputValue)
   {
      // Make room for the new control point at the specified position within the
      // control point array.  The position is determined by the input value of
      // the control point; the control points must be sorted by input value
      // within that array.
      ControlPoint[] newControlPoints = new ControlPoint[controlPointCount + 1];
      
      for (int t = 0; t < (controlPointCount + 1); t++)
         newControlPoints[t] = new ControlPoint();
      
      for (int i = 0; i < controlPointCount; i++) {
         if (i < insertionPos) {
            newControlPoints[i] = controlPoints[i];
         } else {
            newControlPoints[i + 1] = controlPoints[i];
         }
      }

      controlPoints = newControlPoints;
      ++controlPointCount;

      // Now that we've made room for the new control point within the array, add
      // the new control point.
      controlPoints[insertionPos].inputValue  = inputValue;
      controlPoints[insertionPos].outputValue = outputValue;
   }
}


/// This class defines a control point.
///
/// Control points are used for defining splines.
class ControlPoint
{
   /// The input value.
   double inputValue;

   /// The output value that is mapped from the input value.
   double outputValue;
   
   ControlPoint()
   {
      inputValue = 0.0;
      outputValue = 0.0;
   }
   
   ControlPoint(double inputValue, double outputValue)
   {
      this.inputValue = inputValue;
      this.outputValue = outputValue;
   }
   
   
   
}