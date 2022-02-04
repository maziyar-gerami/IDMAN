package parsso.idman.models.users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
    public  class ChangePassword{
        
        public ChangePassword(long time2, int i) {
            this.time = time2;
            this.n = i;
    }
        long time;
        int n;
}