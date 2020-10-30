package net.minecraft.world;
public class Coords
{
public final int[] c;
private final int hash;
public Coords (int x, int y, int z)
{
this.c = new int[] { x, y, z };
 this.hash = ((y * 31 + x) * 31 + z) * 17 + y;
}
@Override
public int hashCode()
{
return this.hash;
}
@Override
public boolean equals(Object o)
{
if(!(o instanceof Coords))return false;
Coords c = (Coords)o;
return this.c[0] == c.c[0] && this.c[1] == c.c[1] && this.c[2] == c.c[2];
}
}
