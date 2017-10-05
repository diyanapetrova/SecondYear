package uk.ac.standrews.cs.cs2001.w08.common;

import uk.ac.standrews.cs.cs2001.w08.impl.Factory;
import uk.ac.standrews.cs.cs2001.w08.interfaces.IFactory;

/**
 * Abstract base class for classes which need to use the Factory class.
 */
public abstract class AbstractFactoryClient {

    private static IFactory factory = Factory.getInstance();

    /**
     * Method which returns an instance of IFactory.
     *
     * @return an instance of Factory
     */
    public static IFactory getFactory() {
        return factory;
    }
}
