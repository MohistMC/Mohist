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

package libnoiseforjava.util;

import libnoiseforjava.exception.ExceptionInvalidParam;
import libnoiseforjava.model.Cylinder;

public class NoiseMapBuilderCylinder extends NoiseMapBuilder
{

   /// Builds a cylindrical noise map.
   ///
   /// This class builds a noise map by filling it with coherent-noise values
   /// generated from the surface of a cylinder.
   ///
   /// This class describes these input values using an (angle, height)
   /// coordinate system.  After generating the coherent-noise value from the
   /// input value, it then "flattens" these coordinates onto a plane so that
   /// it can write the values into a two-dimensional noise map.
   ///
   /// The cylinder model has a radius of 1.0 unit and has infinite height.
   /// The cylinder is oriented along the @a y axis.  Its center is at the
   /// origin.
   ///
   /// The x coordinate in the noise map represents the angle around the
   /// cylinder's y axis.  The y coordinate in the noise map represents the
   /// height above the x-z plane.
   ///
   /// The application must provide the lower and upper angle bounds of the
   /// noise map, in degrees, and the lower and upper height bounds of the
   /// noise map, in units.

   /// Lower angle boundary of the cylindrical noise map, in degrees.
   double lowerAngleBound;

   /// Lower height boundary of the cylindrical noise map, in units.
   double lowerHeightBound;

   /// Upper angle boundary of the cylindrical noise map, in degrees.
   double upperAngleBound;

   /// Upper height boundary of the cylindrical noise map, in units.
   double upperHeightBound;



   public NoiseMapBuilderCylinder () throws ExceptionInvalidParam
   {
      super();
      lowerAngleBound = 0.0;
      lowerHeightBound = 0.0;
      upperAngleBound = 0.0;
      upperHeightBound = 0.0;
   }

   public void build () throws ExceptionInvalidParam
   {
      if (upperAngleBound <= lowerAngleBound
            || upperHeightBound <= lowerHeightBound
            || destWidth <= 0
            || destHeight <= 0
            || sourceModule == null
            || destNoiseMap == null)
         throw  new ExceptionInvalidParam ("Invalid Parameter in NoiseMapBuilderCylinder");


      // Resize the destination noise map so that it can store the new output
      // values from the source model.
      destNoiseMap.setSize (destWidth, destHeight);

      // Create the cylinder model.
      Cylinder cylinderModel = new Cylinder();
      cylinderModel.setModule (sourceModule);

      double angleExtent  = upperAngleBound  - lowerAngleBound ;
      double heightExtent = upperHeightBound - lowerHeightBound;
      double xDelta = angleExtent  / (double)destWidth ;
      double yDelta = heightExtent / (double)destHeight;
      double curAngle  = lowerAngleBound ;
      double curHeight = lowerHeightBound;

      // Fill every point in the noise map with the output values from the model.
      for (int y = 0; y < destHeight; y++)
      {
         curAngle = lowerAngleBound;
         for (int x = 0; x < destWidth; x++)
         {
            float curValue = (float)cylinderModel.getValue (curAngle, curHeight);
            destNoiseMap.setValue(x, y, curValue);
            curAngle += xDelta;
         }
         curHeight += yDelta;
         setCallback (y);
      }
   }

   /// Returns the lower angle boundary of the cylindrical noise map.
   ///
   /// @returns The lower angle boundary of the noise map, in degrees.
   public double getLowerAngleBound ()
   {
      return lowerAngleBound;
   }

   /// Returns the lower height boundary of the cylindrical noise map.
   ///
   /// @returns The lower height boundary of the noise map, in units.
   ///
   /// One unit is equal to the radius of the cylinder.
   public double getLowerHeightBound ()
   {
      return lowerHeightBound;
   }

   /// Returns the upper angle boundary of the cylindrical noise map.
   ///
   /// @returns The upper angle boundary of the noise map, in degrees.
   public double GetUpperAngleBound ()
   {
      return upperAngleBound;
   }

   /// Returns the upper height boundary of the cylindrical noise map.
   ///
   /// @returns The upper height boundary of the noise map, in units.
   ///
   /// One unit is equal to the radius of the cylinder.
   public double getUpperHeightBound ()
   {
      return upperHeightBound;
   }

   /// Sets the coordinate boundaries of the noise map.
   ///
   /// @param lowerAngleBound The lower angle boundary of the noise map,
   /// in degrees.
   /// @param upperAngleBound The upper angle boundary of the noise map,
   /// in degrees.
   /// @param lowerHeightBound The lower height boundary of the noise
   /// map, in units.
   /// @param upperHeightBound The upper height boundary of the noise
   /// map, in units.
   ///
   /// @pre The lower angle boundary is less than the upper angle
   /// boundary.
   /// @pre The lower height boundary is less than the upper height
   /// boundary.
   ///
   /// @throw noise::ExceptionInvalidParam See the preconditions.
   ///
   /// One unit is equal to the radius of the cylinder.
   public void setBounds (double lowerAngleBound, double upperAngleBound,
         double lowerHeightBound, double upperHeightBound) throws ExceptionInvalidParam
   {
      if (lowerAngleBound >= upperAngleBound
            || lowerHeightBound >= upperHeightBound)
         throw new ExceptionInvalidParam ("Invalid Parameter in NoiseMapBuilder Cylinder");

      this.lowerAngleBound  = lowerAngleBound ;
      this.upperAngleBound  = upperAngleBound ;
      this.lowerHeightBound = lowerHeightBound;
      this.upperHeightBound = upperHeightBound;
   }

   public double getUpperAngleBound()
   {
      return upperAngleBound;
   }

   public void setLowerAngleBound(double lowerAngleBound)
   {
      this.lowerAngleBound = lowerAngleBound;
   }

   public void setLowerHeightBound(double lowerHeightBound)
   {
      this.lowerHeightBound = lowerHeightBound;
   }

   public void setUpperAngleBound(double upperAngleBound)
   {
      this.upperAngleBound = upperAngleBound;
   }

   public void setUpperHeightBound(double upperHeightBound)
   {
      this.upperHeightBound = upperHeightBound;
   }

}
