package coollaafi.wot.web.post;

import coollaafi.wot.web.ai.AIService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostConverter postConverter;

    private final AIService aiService;

    @Transactional
    public PostResponseDTO.PostCreateLookBookResultDTO createPostLookbook(PostRequestDTO.PostCreateLookBookRequestDTO requestDTO) {
        URL lookbookImage = aiService.generateLookBookImage(requestDTO.getOotdImage());
        Post post = postConverter.toEntity(requestDTO, lookbookImage);
        Post savedPost = postRepository.save(post);
        return postConverter.toCreateOotdResultDTO(savedPost);
    }

    @Transactional
    public PostResponseDTO.PostCreateResultDTO createPost(PostRequestDTO.PostCreateRequestDTO requestDTO) {
        Post post = postRepository.findById(requestDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));
        post.setDescription(requestDTO.getDescription());
        post.setPostCondition(requestDTO.getPostCondition());
        Post savedPost = postRepository.save(post);
        return postConverter.toCreateResultDTO(savedPost);
    }


}
