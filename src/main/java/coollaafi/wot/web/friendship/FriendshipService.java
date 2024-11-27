package coollaafi.wot.web.friendship;

import coollaafi.wot.apiPayload.code.status.ErrorStatus;
import coollaafi.wot.web.friendRequest.FriendRequest;
import coollaafi.wot.web.friendRequest.FriendRequestHandler;
import coollaafi.wot.web.friendRequest.FriendRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipConverter friendshipConverter;

    @Transactional
    public FriendshipDTO.responseDTO acceptFriendRequest(Long id){
        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_NOT_FOUND));
        if(request.getStatus() != FriendRequest.RequestStatus.PENDING){
            throw new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_ALREADY_PROCESSED);
        }
        Friendship friendship = friendshipConverter.toEntity(request);
        friendshipRepository.save(friendship);

        // Update request status
        request.setStatus(FriendRequest.RequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        return friendshipConverter.toResult(friendship);
    }

    @Transactional
    public void rejectFriendRequest(Long id){
        FriendRequest request = friendRequestRepository.findById(id)
                .orElseThrow(() -> new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_NOT_FOUND));
        if(request.getStatus() != FriendRequest.RequestStatus.PENDING){
            throw new FriendRequestHandler(ErrorStatus.FRIEND_REQUEST_ALREADY_PROCESSED);
        }

        // Update request status
        request.setStatus(FriendRequest.RequestStatus.REJECTED);
        friendRequestRepository.save(request);
    }
}
