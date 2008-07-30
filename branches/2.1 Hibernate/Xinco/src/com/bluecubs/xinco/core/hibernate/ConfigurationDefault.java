package com.bluecubs.xinco.core.hibernate;

/**
 *
 * @author Javier A. Ortiz Bultron <javier.ortiz.78@gmail.com>
 */
public enum ConfigurationDefault {

    MIN_SIZE {

        public int getDefaultValue() {
            return 5;
        }
    },
    MAX_SIZE {

        public int getDefaultValue() {
            return 20;
        }
    }, TIMEOUT {

        public int getDefaultValue() {
            return 1800;
        }
    }, MAX_STATEMENTS {

        public int getDefaultValue() {
            return 50;
        }
    };
}
