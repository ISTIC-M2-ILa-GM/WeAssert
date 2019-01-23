package fr.istic.gm.weassert;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class TestData {

    private static final PodamFactory FACTORY = new PodamFactoryImpl();

    /**
     * Generate an object
     * usage: some(YourObject.class)
     *
     * @param clazz the class
     * @param <T>   the return type
     * @return a new object with all data
     */
    public static final <T> T some(Class<T> clazz) {
        return FACTORY.manufacturePojoWithFullData(clazz);
    }
}
