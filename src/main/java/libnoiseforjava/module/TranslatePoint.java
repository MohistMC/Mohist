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

public class TranslatePoint extends ModuleBase
{
   /// Noise module that moves the coordinates of the input value before
   /// returning the output value from a source module.
   ///
   /// The getValue() method moves the ( @a x, @a y, @a z ) coordinates of
   /// the input value by a translation amount before returning the output
   /// value from the source module.  To set the translation amount, call
   /// the setTranslation() method.  To set the translation amount to
   /// apply to the individual @a x, @a y, or @a z coordinates, call the
   /// setXTranslation(), setYTranslation() or setZTranslation() methods,
   /// respectively.
   ///
   /// This noise module requires one source module.


   /// Default translation factor applied to the @a x coordinate for the
   /// TranslatePoint noise module.
   static final double DEFAULT_TRANSLATE_POINT_X = 0.0;

   /// Default translation factor applied to the @a y coordinate for the
   /// TranslatePoint noise module.
   static final double DEFAULT_TRANSLATE_POINT_Y = 0.0;

   /// Default translation factor applied to the @a z coordinate for the
   /// TranslatePoint noise module.
   static final double DEFAULT_TRANSLATE_POINT_Z = 0.0;

   /// Translation amount applied to the @a x coordinate of the input
   /// value.
   double xTranslation;

   /// Translation amount applied to the @a y coordinate of the input
   /// value.
   double yTranslation;

   /// Translation amount applied to the @a z coordinate of the input
   /// value.
   double zTranslation;

   public TranslatePoint (ModuleBase sourceModule) throws ExceptionInvalidParam
   {
      super(1);
      setSourceModule(0, sourceModule);
      xTranslation = DEFAULT_TRANSLATE_POINT_X;
      yTranslation = DEFAULT_TRANSLATE_POINT_Y;
      zTranslation = DEFAULT_TRANSLATE_POINT_Z;

   }

   public double getValue (double x, double y, double z)
   {
      assert (sourceModules[0] != null);

      return sourceModules[0].getValue (x + xTranslation, y + yTranslation,
            z + zTranslation);
   }

   /// Returns the translation amount to apply to the @a x coordinate of
   /// the input value.
   ///
   /// @returns The translation amount to apply to the @a x coordinate.
   public double getXTranslation ()
   {
      return xTranslation;
   }

   /// Returns the translation amount to apply to the @a y coordinate of
   /// the input value.
   ///
   /// @returns The translation amount to apply to the @a y coordinate.
   public double getYTranslation ()
   {
      return yTranslation;
   }

   /// Returns the translation amount to apply to the @a z coordinate of
   /// the input value.
   ///
   /// @returns The translation amount to apply to the @a z coordinate.
   public double getZTranslation ()
   {
      return zTranslation;
   }

   /// Sets the translation amount to apply to the input value.
   ///
   /// @param translation The translation amount to apply.
   ///
   /// The getValue() method moves the ( @a x, @a y, @a z ) coordinates
   /// of the input value by a translation amount before returning the
   /// output value from the source module
   public void setTranslation (double translation)
   {
      this.xTranslation = translation;
      this.yTranslation = translation;
      this.zTranslation = translation;
   }

   /// Sets the translation amounts to apply to the ( @a x, @a y, @a z )
   /// coordinates of the input value.
   ///
   /// @param xTranslation The translation amount to apply to the @a x
   /// coordinate.
   /// @param yTranslation The translation amount to apply to the @a y
   /// coordinate.
   /// @param zTranslation The translation amount to apply to the @a z
   /// coordinate.
   ///
   /// The getValue() method moves the ( @a x, @a y, @a z ) coordinates
   /// of the input value by a translation amount before returning the
   /// output value from the source module
   public void setTranslation (double xTranslation, double yTranslation,
         double zTranslation)
   {
      this.xTranslation = xTranslation;
      this.yTranslation = yTranslation;
      this.zTranslation = zTranslation;
   }

   /// Sets the translation amount to apply to the @a x coordinate of the
   /// input value.
   ///
   /// @param xTranslation The translation amount to apply to the @a x
   /// coordinate.
   ///
   /// The getValue() method moves the ( @a x, @a y, @a z ) coordinates
   /// of the input value by a translation amount before returning the
   /// output value from the source module
   public void setXTranslation (double xTranslation)
   {
      this.xTranslation = xTranslation;
   }

   /// Sets the translation amount to apply to the @a y coordinate of the
   /// input value.
   ///
   /// @param yTranslation The translation amount to apply to the @a y
   /// coordinate.
   ///
   /// The getValue() method moves the ( @a x, @a y, @a z ) coordinates
   /// of the input value by a translation amount before returning the
   /// output value from the source module
   public void setYTranslation (double yTranslation)
   {
      this.yTranslation = yTranslation;
   }

   /// Sets the translation amount to apply to the @a z coordinate of the
   /// input value.
   ///
   /// @param zTranslation The translation amount to apply to the @a z
   /// coordinate.
   ///
   /// The getValue() method moves the ( @a x, @a y, @a z ) coordinates
   /// of the input value by a translation amount before returning the
   /// output value from the source module
   public void setZTranslation (double zTranslation)
   {
      this.zTranslation = zTranslation;
   }

}
