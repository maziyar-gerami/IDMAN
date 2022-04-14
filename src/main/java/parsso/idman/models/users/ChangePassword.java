package parsso.idman.models.users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
    public  class ChangePassword{
        
        public ChangePassword(long time, int n) {
            this.time = time;
            this.n = n;
    }
        long time;
        int n;
}