package nbody

import nbody.model.Particle

import javax.vecmath.Point3d
import java.util.Random
import javax.vecmath.Vector3d

/**
 * Created by v2 on 13/04/15.
 */

Particle p = new Particle("Sun",400d,new Point3d(),new Vector3d())
print p
p1 = p.clone();
print p
assert p == p1
assert p.is(p1)