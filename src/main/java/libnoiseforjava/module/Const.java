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

public class Const extends ModuleBase
{
   /// Noise module that outputs a constant value.
   ///
   /// @image html moduleconst.png
   ///
   /// To specify the constant value, call the setConstValue() method.
   ///
   /// This noise module is not useful by itself, but it is often used as a
   /// source module for other noise modules.
   ///
   /// This noise module does not require any source modules.

   /// Default constant value for the Const noise module.
   static final double DEFAULT_CONST_VALUE = 0.0;

   double constValue;

   public Const ()
   {
      super(0);
      this.constValue = DEFAULT_CONST_VALUE;
   }

   public double getValue (double x, double y, double z)
   {
      return constValue;
   }

   /// Sets the constant output value for this noise module.
   ///
   /// @param constValue The constant output value for this noise module.
   public void setConstValue (double constValue)
   {
      this.constValue = constValue;
   }

}
