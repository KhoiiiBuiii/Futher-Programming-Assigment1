package system.manager;
/**
 * @author <Bui Minh Khoi - s3929015>
 */
import system.entity.Claim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClaimProcessManagerImpl implements ClaimProcessManager {
    private final Map<String, Claim> claimMap;

    public ClaimProcessManagerImpl() {
        this.claimMap = new HashMap<>();
    }

    @Override
    public void add(Claim claim) {
        claimMap.put(claim.getId(), claim);
    }

    @Override
    public void update(Claim claim) {
        if (claimMap.containsKey(claim.getId())) {
            claimMap.put(claim.getId(), claim);
        }
    }

    @Override
    public void delete(String claimId) {
        claimMap.remove(claimId);
    }

    @Override
    public Claim getOne(String claimId) {
        return claimMap.get(claimId);
    }

    @Override
    public List<Claim> getAll() {
        return new ArrayList<>(claimMap.values());
    }
}
