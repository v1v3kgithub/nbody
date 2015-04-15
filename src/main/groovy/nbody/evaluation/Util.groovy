package nbody.evaluation

/**
 * Created by v2 on 13/04/15.
 */
import nbody.model.Particle

import javax.vecmath.Vector3d

class Util {

    public static final double G = 6.67259E-11 ;  // in m3/s2/kg    as per jpl planetary satellites page

    public void calculateForceBetweenParticles(Map.Entry<Particle,Particle> entry) {
        Particle p1 = entry.key
        Particle p2 = entry.value

        Vector3d distVect = new Vector3d();
        distVect.sub(p1.location, p2.location);    // pos - withObject.pos
        double r = distVect.length();
        double force  =  (p1.mass / r) * G * (p2.mass / r) ;
        double ratio = force / r;
        Vector3d nowForce = new Vector3d(distVect);
        nowForce.scale(ratio);

        p1.addForce(nowForce);

        nowForce.negate();
        p2.addToForce(nowForce);
    }

    public void updateLocationAndVelocity(List<Particle> allParticles,
                                          double deltaT) {
        allParticles.each{it.updateVelocityAndPosition(deltaT)}
    }

    public Map<Particle,Particle> createPairOfParticles(List<Particle> allParticles){
        int noParticles = allParticles.size();
        Map<Particle,Particle> pairOfParticles = new HashMap()
        for (int i =0;i<noParticles;i++) {
            Particle key = allParticles[i]
            for (int j = i+1;j<noParticles;j++) {
                Particle value = allParticles[j]
                pairOfParticles.put(key,value)
            }
        }
        return pairOfParticles.asImmutable()
    }
}
