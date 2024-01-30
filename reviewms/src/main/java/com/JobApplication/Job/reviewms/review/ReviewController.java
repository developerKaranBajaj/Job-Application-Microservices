package com.JobApplication.Job.reviewms.review;

import com.JobApplication.Job.reviewms.review.messaging.ReviewMessageProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;
    private ReviewMessageProducer reviewMessageProducer;

    public ReviewController(ReviewService reviewService, ReviewMessageProducer reviewMessageProducer) {
        this.reviewService = reviewService;
        this.reviewMessageProducer = reviewMessageProducer;
    }

    @GetMapping()
    public ResponseEntity<List<Review>> getAllReviews(@RequestParam Long companyId){
        return new ResponseEntity<>(reviewService.getAllReview(companyId),
                HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<String> addReview(@RequestParam Long companyId, @RequestBody Review review){
        if(reviewService.addReview(companyId,review)) {
            reviewMessageProducer.sendMessage(review);
            return new ResponseEntity<>("Review Added Successfully", HttpStatus.OK);
        }

        return new ResponseEntity<>("Company not found", HttpStatus.OK);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReviewById( @PathVariable Long reviewId){

        return new ResponseEntity<>(reviewService.getReview(reviewId),HttpStatus.OK);
    }
    @PutMapping("{reviewId}")
    public ResponseEntity<String> updateReview(@PathVariable Long reviewId, @RequestBody Review review){

        if(reviewService.updateReview(reviewId, review))
            return new ResponseEntity<>("Review update successfully", HttpStatus.OK);
        return new ResponseEntity<>("Company not found or Review doesn't exist", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId){

        if(reviewService.deleteReview(reviewId)){
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Company not found or Review doesn't exist", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/averageRating")
    public Double getAverageRating(@RequestParam Long companyId){
        List<Review> reviewList= reviewService.getAllReview(companyId);
        return reviewList.stream().mapToDouble(Review::getRating).average().orElse(0.0);
    }

}
