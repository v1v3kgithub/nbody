package nbody.evaluation

/**
 * Created by v2 on 13/04/15.
 */
import nbody.model.Particle

import javax.vecmath.Vector3d

class Util {

    public static final double G = 6.67259E-11 ;  // in m3/s2/kg    as per jpl planetary satellites page

    public void calculateForceBetweenParticles(Tuple2<Particle,Particle> particlePair) {
        Particle p1 = particlePair.first
        Particle p2 = particlePair.second

        Vector3d distVect = new Vector3d();
        distVect.sub(p1.location, p2.location);    // pos - withObject.pos
        double r = distVect.length();
        double force  =  (p1.mass / r) * G * (p2.mass / r) ;
        double ratio = force / r;
        Vector3d nowForce = new Vector3d(distVect);
        nowForce.scale(ratio);

        p1.addForce(nowForce);

        nowForce.negate();
        p2.addForce(nowForce);
    }

    public void updateLocationAndVelocity(List<Particle> allParticles,
                                          double deltaT) {
        // Can be run in parallel
        allParticles.each{
            it.updateVelocityAndPosition(deltaT)
        }
    }

    public List<Tuple2<Particle,Particle>> createPairOfParticles(List<Particle> allParticles){
        int noParticles = allParticles.size();
        def pairOfParticles = new ArrayList<Tuple2<Particle,Particle>>()
        for (int i =0;i<noParticles;i++) {
            Particle p1 = allParticles[i]
            for (int j = i+1;j<noParticles;j++) {
                Particle p2 = allParticles[j]
                pairOfParticles.add(new Tuple2<Particle, Particle>(p1,p2))
            }
        }
        return pairOfParticles.asImmutable()
    }
}
