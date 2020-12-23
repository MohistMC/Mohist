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

public class RotatePoint extends ModuleBase
{
   /// Noise module that rotates the input value around the origin before
   /// returning the output value from a source module.
   ///
   /// The getValue() method rotates the coordinates of the input value
   /// around the origin before returning the output value from the source
   /// module.  To set the rotation angles, call the setAngles() method.  To
   /// set the rotation angle around the individual @a x, @a y, or @a z axes,
   /// call the setXAngle(), setYAngle() or setZAngle() methods,
   /// respectively.
   ///
   /// The coordinate system of the input value is assumed to be
   /// "left-handed" (@a x increases to the right, @a y increases upward,
   /// and @a z increases inward.)
   ///
   /// This noise module requires one source module.

   /// Default @a x rotation angle for the RotatePoint noise
   /// module.
   static final double DEFAULT_ROTATE_X = 0.0;

   /// Default @a y rotation angle for the RotatePoint noise
   /// module.
   static final double DEFAULT_ROTATE_Y = 0.0;

   /// Default @a z rotation angle for the RotatePoint noise
   /// module.
   static final double DEFAULT_ROTATE_Z = 0.0;


   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double x1Matrix;

   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double x2Matrix;

   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double x3Matrix;

   /// @a x rotation angle applied to the input value, in degrees.
   double xAngle;

   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double y1Matrix;

   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double y2Matrix;

   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double y3Matrix;

   /// @a y rotation angle applied to the input value, in degrees.
   double yAngle;

   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double z1Matrix;

   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double z2Matrix;

   /// An entry within the 3x3 rotation matrix used for rotating the
   /// input value.
   double z3Matrix;

   /// @a z rotation angle applied to the input value, in degrees.
   double zAngle;


   public RotatePoint (ModuleBase sourceModule) throws ExceptionInvalidParam
   {
      super(1);
      setSourceModule(0, sourceModule);
      setAngles (DEFAULT_ROTATE_X, DEFAULT_ROTATE_Y, DEFAULT_ROTATE_Z);
   }

   public double getValue (double x, double y, double z)
   {
      assert (sourceModules[0] != null);

      double nx = (x1Matrix * x) + (y1Matrix * y) + (z1Matrix * z);
      double ny = (x2Matrix * x) + (y2Matrix * y) + (z2Matrix * z);
      double nz = (x3Matrix * x) + (y3Matrix * y) + (z3Matrix * z);
      return sourceModules[0].getValue (nx, ny, nz);
   }

   public void setAngles (double xAngle, double yAngle,
         double zAngle)
   {
      double xCos, yCos, zCos, xSin, ySin, zSin;
      xCos = Math.cos (Math.toRadians(xAngle));
      yCos = Math.cos (Math.toRadians(yAngle));
      zCos = Math.cos (Math.toRadians(zAngle));
      xSin = Math.sin (Math.toRadians(xAngle));
      ySin = Math.sin (Math.toRadians(yAngle));
      zSin = Math.sin (Math.toRadians(zAngle));

      x1Matrix = ySin * xSin * zSin + yCos * zCos;
      y1Matrix = xCos * zSin;
      z1Matrix = ySin * zCos - yCos * xSin * zSin;
      x2Matrix = ySin * xSin * zCos - yCos * zSin;
      y2Matrix = xCos * zCos;
      z2Matrix = -yCos * xSin * zCos - ySin * zSin;
      x3Matrix = -ySin * xCos;
      y3Matrix = xSin;
      z3Matrix = yCos * xCos;

      this.xAngle = xAngle;
      this.yAngle = yAngle;
      this.zAngle = zAngle;
   }

   /// Returns the rotation angle around the @a x axis to apply to the
   /// input value.
   ///
   /// @returns The rotation angle around the @a x axis, in degrees.
   public double getXAngle ()
   {
      return xAngle;
   }

   /// Returns the rotation angle around the @a y axis to apply to the
   /// input value.
   ///
   /// @returns The rotation angle around the @a y axis, in degrees.
   public double getYAngle ()
   {
      return yAngle;
   }

   /// Returns the rotation angle around the @a z axis to apply to the
   /// input value.
   ///
   /// @returns The rotation angle around the @a z axis, in degrees.
   public double getZAngle ()
   {
      return zAngle;
   }

   /// Sets the rotation angle around the @a x axis to apply to the input
   /// value.
   ///
   /// @param xAngle The rotation angle around the @a x axis, in degrees.
   ///
   /// The getValue() method rotates the coordinates of the input value
   /// around the origin before returning the output value from the
   /// source module.
   public void setXAngle (double xAngle)
   {
      setAngles (xAngle, this.yAngle, this.zAngle);
   }

   /// Sets the rotation angle around the @a y axis to apply to the input
   /// value.
   ///
   /// @param yAngle The rotation angle around the @a y axis, in degrees.
   ///
   /// The getValue() method rotates the coordinates of the input value
   /// around the origin before returning the output value from the
   /// source module.
   public void SetYAngle (double yAngle)
   {
      setAngles (this.xAngle, yAngle, this.zAngle);
   }

   /// Sets the rotation angle around the @a z axis to apply to the input
   /// value.
   ///
   /// @param zAngle The rotation angle around the @a z axis, in degrees.
   ///
   /// The getValue() method rotates the coordinates of the input value
   /// around the origin before returning the output value from the
   /// source module.
   public void SetZAngle (double zAngle)
   {
      setAngles (this.xAngle, this.yAngle, zAngle);
   }
   
}
