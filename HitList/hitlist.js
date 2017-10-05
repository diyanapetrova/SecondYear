var app = angular.module('myApp', []);
app.controller('myCtrl', function($scope, $http, $window) {
   
   $scope.search = function() {
       //change the query url if any text is inputed in the fulTesxt field
       if($scope.userQuery!=undefined){
           $scope.query = $scope.userQuery;
       }else{
            $scope.query = "*";

       }
       //add the needed strings if searching in a particular field
       if($scope.selItem!=undefined){
           $scope.query = $scope.selItem + $scope.query + "*";
       }
       
       //http request to solr
        $http({
        method : "GET",
        url : "http://localhost:8983/solr/hitlist/select?&"+
        "fq=publishedAt:["+$scope.startDate.toISOString()+"%20TO%20"+$scope.endDate.toISOString()+"]&"+
        "indent=on&q="+ $scope.query + "&rows=50&sort=position%20asc,%20publishedAt%20desc&wt=json"
        }).then(function mySucces(response) {
            $scope.videos = filter(response.data.response.docs);
        }, function myError(response) {
        $window.alert("Error!");
        }
        );
    };

    //filter the duplicates in the list
    function filter(rawData){
        var keys = new Array();
        var videos = new Array();
        for(var i =0;i<rawData.length;i++){
            if(keys.indexOf(rawData[i].videoId)===-1){
                    keys.push(rawData[i].videoId);
                    videos.push(rawData[i]);
                }
            if(videos.length==10){
                return videos;
            }
        }
        return videos;
    };

    //display the the description, published date and the video
    $scope.playVideo = function(videoData) {
        //set all details of the video
        $scope.title = videoData.title;
        $scope.description = videoData.description;
        $scope.publishedDate = moment(videoData.publishedAt).format('MMMM Do YYYY');
        var videoFrame = document.getElementById("iframe");
        videoFrame.src = "https://www.youtube.com/embed/" + videoData.videoId;

        videoSection = document.getElementById("video");
        dSection = document.getElementById("defaultContent");
        //hide defaultContent and show video details
        dSection.style.display = "none";
        videoSection.style.display = "block";
        
    };

});