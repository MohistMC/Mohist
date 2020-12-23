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

import libnoiseforjava.exception.ExceptionInvalidParam;

public class ScalePoint extends ModuleBase
{

   /// Noise module that scales the coordinates of the input value before
   /// returning the output value from a source module.
   ///
   /// The getValue() method multiplies the ( @a x, @a y, @a z ) coordinates
   /// of the input value with a scaling factor before returning the output
   /// value from the source module.  To set the scaling factor, call the
   /// setScale() method.  To set the scaling factor to apply to the
   /// individual @a x, @a y, or @a z coordinates, call the setXScale(),
   /// setYScale() or setZScale() methods, respectively.
   ///
   /// This noise module requires one source module.

   // Default scaling factor applied to the @a x coordinate for the
   /// ScalePoint noise module.
   static final double DEFAULT_SCALE_POINT_X = 1.0;

   /// Default scaling factor applied to the @a y coordinate for the
   /// ScalePoint noise module.
   static final double DEFAULT_SCALE_POINT_Y = 1.0;

   /// Default scaling factor applied to the @a z coordinate for the
   /// ScalePoint noise module.
   static final double DEFAULT_SCALE_POINT_Z = 1.0;

   /// Scaling factor applied to the @a x coordinate of the input value.
   double xScale;

   /// Scaling factor applied to the @a y coordinate of the input value.
   double yScale;

   /// Scaling factor applied to the @a z coordinate of the input value.
   double zScale;


   public ScalePoint (ModuleBase sourceModule) throws ExceptionInvalidParam
   {
      super(1);
      setSourceModule(0, sourceModule);

      xScale = DEFAULT_SCALE_POINT_X;
      yScale = DEFAULT_SCALE_POINT_Y;
      zScale = DEFAULT_SCALE_POINT_Z; 
   }

   public double getValue (double x, double y, double z)
   {
      assert (sourceModules[0] != null);

      return sourceModules[0].getValue (x * xScale, y * yScale,
            z * zScale);
   }

   /// Returns the scaling factor applied to the @a x coordinate of the
   /// input value.
   ///
   /// @returns The scaling factor applied to the @a x coordinate.
   public double getXScale ()
   {
      return xScale;
   }

   /// Returns the scaling factor applied to the @a y coordinate of the
   /// input value.
   ///
   /// @returns The scaling factor applied to the @a y coordinate.
   public double getYScale ()
   {
      return yScale;
   }

   /// Returns the scaling factor applied to the @a z coordinate of the
   /// input value.
   ///
   /// @returns The scaling factor applied to the @a z coordinate.
   public double getZScale ()
   {
      return zScale;
   }

   /// Sets the scaling factor to apply to the input value.
   ///
   /// @param scale The scaling factor to apply.
   ///
   /// The getValue() method multiplies the ( @a x, @a y, @a z )
   /// coordinates of the input value with a scaling factor before
   /// returning the output value from the source module.
   public void setScale (double scale)
   {
      this.xScale = scale;
      this.yScale = scale;
      this.zScale = scale;
   }

   /// Sets the scaling factor to apply to the ( @a x, @a y, @a z )
   /// coordinates of the input value.
   ///
   /// @param xScale The scaling factor to apply to the @a x coordinate.
   /// @param yScale The scaling factor to apply to the @a y coordinate.
   /// @param zScale The scaling factor to apply to the @a z coordinate.
   ///
   /// The getValue() method multiplies the ( @a x, @a y, @a z )
   /// coordinates of the input value with a scaling factor before
   /// returning the output value from the source module.
   public void setScale (double xScale, double yScale, double zScale)
   {
      this.xScale = xScale;
      this.yScale = yScale;
      this.zScale = zScale;
   }

   /// Sets the scaling factor to apply to the @a x coordinate of the
   /// input value.
   ///
   /// @param xScale The scaling factor to apply to the @a x coordinate.
   ///
   /// The getValue() method multiplies the ( @a x, @a y, @a z )
   /// coordinates of the input value with a scaling factor before
   /// returning the output value from the source module.
   public void setXScale (double xScale)
   {
      this.xScale = xScale;
   }

   /// Sets the scaling factor to apply to the @a y coordinate of the
   /// input value.
   ///
   /// @param yScale The scaling factor to apply to the @a y coordinate.
   ///
   /// The getValue() method multiplies the ( @a x, @a y, @a z )
   /// coordinates of the input value with a scaling factor before
   /// returning the output value from the source module.
   public void setYScale (double yScale)
   {
      this.yScale = yScale;
   }

   /// Sets the scaling factor to apply to the @a z coordinate of the
   /// input value.
   ///
   /// @param zScale The scaling factor to apply to the @a z coordinate.
   ///
   /// The getValue() method multiplies the ( @a x, @a y, @a z )
   /// coordinates of the input value with a scaling factor before
   /// returning the output value from the source module.
   public void setZScale (double zScale)
   {
      this.zScale = zScale;
   }

}
