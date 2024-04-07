package system.manager;
/**
 * @author <Bui Minh Khoi - s3929015>
 */
import system.entity.Claim;

import java.util.List;

public interface ClaimProcessManager {
    void add(Claim claim);

    void update(Claim claim);

    void delete(String claimId);

    Claim getOne(String claimId);

    List<Claim> getAll();
}
