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

public class Exponent extends ModuleBase
{
   /// Noise module that maps the output value from a source module onto an
   /// exponential curve.
   ///
   /// Because most noise modules will output values that range from -1.0 to
   /// +1.0, this noise module first normalizes this output value (the range
   /// becomes 0.0 to 1.0), maps that value onto an exponential curve, then
   /// rescales that value back to the original range.
   ///
   /// This noise module requires one source module.

   /// Default exponent for the Exponent noise module.
   static final double DEFAULT_EXPONENT = 1.0;


   /// Exponent to apply to the output value from the source module.
   double exponent;

   public Exponent (ModuleBase sourceModule) throws ExceptionInvalidParam
   {
      super(1);
      setSourceModule(0, sourceModule);
      exponent = DEFAULT_EXPONENT;

   }

   public double getValue (double x, double y, double z)
   {
      assert (sourceModules[0] != null);

      double value = sourceModules[0].getValue (x, y, z);
      return (Math.pow (Math.abs ((value + 1.0) / 2.0), exponent) * 2.0 - 1.0);
   }

   /// Returns the exponent value to apply to the output value from the
   /// source module.
   ///
   /// @returns The exponent value.
   public double getExponent ()
   {
      return exponent;
   }

   public void setExponent(double exponent)
   {
      this.exponent = exponent;
   }

}
