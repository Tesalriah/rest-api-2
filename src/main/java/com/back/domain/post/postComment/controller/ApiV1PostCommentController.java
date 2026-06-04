package com.back.domain.post.postComment.controller;

import com.back.domain.meber.member.entity.Member;
import com.back.domain.meber.member.service.MemberService;
import com.back.domain.post.post.entity.Post;
import com.back.domain.post.post.service.PostService;
import com.back.domain.post.postComment.dto.PostCommentDto;
import com.back.domain.post.postComment.dto.PostCommentModifyRequestBody;
import com.back.domain.post.postComment.dto.PostCommentWriteRequestBody;
import com.back.domain.post.postComment.entity.PostComment;
import com.back.global.rq.Rq;
import com.back.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
@RestController
@Tag(name="ApiV1PostCommentController", description = "API 댓글 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class ApiV1PostCommentController {
    private final PostService postService;
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping
    @Operation(summary = "다건 조회")
    public List<PostCommentDto> getItems(
            @PathVariable long postId
    ) {
        Post post = postService.findById(postId);

        return post
                .getComments()
                .stream()
                .map(PostCommentDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "단건 조회")
    public PostCommentDto getItem(
            @PathVariable long postId,
            @PathVariable long id
    ) {
        Post post = postService.findById(postId);

        PostComment postComment = post.findCommentById(id).get();

        return new PostCommentDto(postComment);
    }

    @PostMapping
    @ResponseBody
    @Operation(summary = "작성")
    public RsData<PostCommentDto> write(@Valid @RequestBody PostCommentWriteRequestBody request,
                                        @PathVariable long postId){
        Member actor = rq.getActor();

        Post post = postService.findById(postId);

        PostComment postComment = postService.writeComment(actor, post, request.content());

        postService.flush();
        return new RsData<>("201-1", "%d번 댓글이 작성되었습니다.".formatted(postComment.getId()), new PostCommentDto(postComment));
    }

    @Transactional
    @DeleteMapping("/{id}")
    @Operation(summary = "삭제")
    public RsData<Void> delete(
            @PathVariable long postId,
            @PathVariable long id
    ) {
        Member actor = rq.getActor();

        Post post = postService.findById(postId);

        PostComment postComment = post.findCommentById(id).get();

        postComment.checkActorCanDelete(actor);

        postService.deleteComment(post, postComment);

        return new RsData<>("200-1", "%d번 댓글이 삭제되었습니다.".formatted(id));
    }

    @Transactional
    @PutMapping("/{id}")
    @Operation(summary = "수정")
    public RsData<PostCommentDto> modify(@PathVariable long postId,@PathVariable long id,
                                         @Valid @RequestBody PostCommentModifyRequestBody reqBody){
        Member actor = rq.getActor();

        Post post = postService.findById(postId);

        PostComment postComment = post.findCommentById(id).get();

        postComment.checkActorCanModify(actor);

        postService.modifyComment(postComment, reqBody.content());

        return new RsData<>(
                "200-1",
                "%d번 댓글이 수정되었습니다.".formatted(id)
        );
    }
}