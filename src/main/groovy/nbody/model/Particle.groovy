package nbody.model

import javax.vecmath.Vector3d
import javax.vecmath.Point3d
/**
 * Created by v2 on 13/04/15.
 */

class Particle {
    final String id
    final double mass
    final Point3d location
    final Vector3d velocity
    final Vector3d force

    public Particle(String id, double mass, Point3d location, Vector3d velocity,
             Vector3d force) {
        this.id = id
        this.mass = mass
        this.location = location
        this.velocity = velocity
        this.force = force
    }

    public Particle(String id, double mass, Point3d location, Vector3d velocity) {
        this(id,mass,location,velocity,new Vector3d())
    }

    Particle(String id,double mass, Point3d location) {
        this(id,mass,location,new Vector3d())
    }

    public synchronized void addForce(Vector3d forceToAdd) {
        force.add(forceToAdd)
    }

    public synchronized void updateVelocityAndPosition(double deltaT){
        Vector3d thisAcc = new Vector3d(this.force)
        thisAcc.scale(1/mass)
        Vector3d deltaV = new Vector3d(thisAcc)
        deltaV.scale(deltaT)
        this.velocity.add(deltaV)
        Vector3d averageV = new Vector3d(deltaV)
        averageV.scale(-0.5,velocity)
        Vector3d deltaPos = new Vector3d(averageV)
        deltaPos.scale(deltaT)
        this.location.add(deltaPos)
        // Reset the force
        force.set(0,0,0)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Particle particle = (Particle) o

        if (Double.compare(particle.mass, mass) != 0) return false
        if (force != particle.force) return false
        if (id != particle.id) return false
        if (location != particle.location) return false
        if (velocity != particle.velocity) return false

        return true
    }

    int hashCode() {
        int result
        long temp
        result = id.hashCode()
        temp = mass != +0.0d ? Double.doubleToLongBits(mass) : 0L
        result = 31 * result + (int) (temp ^ (temp >>> 32))
        result = 31 * result + location.hashCode()
        result = 31 * result + velocity.hashCode()
        result = 31 * result + force.hashCode()
        return result
    }

    @Override
    public String toString() {
        return "Particle{" +
                "id='" + id + '\'' +
                ", mass=" + mass +
                ", location=" + location +
                ", velocity=" + velocity +
                ", force=" + force +
                '}';
    }
}
