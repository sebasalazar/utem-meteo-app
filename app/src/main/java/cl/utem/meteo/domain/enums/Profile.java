package cl.utem.meteo.domain.enums;

public enum Profile {
    VIEWER {
        @Override
        public int getCode() {
            return 0;
        }

        @Override
        public String getLabel() {
            return "Visor";
        }
    }, STAFF {
        @Override
        public int getCode() {
            return 1;
        }

        @Override
        public String getLabel() {
            return "Colaborador";
        }
    }, API {
        @Override
        public int getCode() {
            return 2;
        }

        @Override
        public String getLabel() {
            return "API";
        }
    }, ADMIN {
        @Override
        public int getCode() {
            return 3;
        }

        @Override
        public String getLabel() {
            return "Administrador";
        }
    }, ROOT {
        @Override
        public int getCode() {
            return 4;
        }

        @Override
        public String getLabel() {
            return "Super Admin";
        }
    };

    public abstract int getCode();

    public abstract String getLabel();
}
