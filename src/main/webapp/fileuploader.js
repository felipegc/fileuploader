var fileuploader = angular.module('fileuploader', []);

fileuploader.controller('mainController', ['$scope', '$filter','$http','$timeout', function($scope, $filter, $http, $timeout) {

    $scope.owner = '';
    $scope.numChar = 5;

    $scope.lowercaseowner = function() {
      return $filter('lowercase')($scope.owner);
    };

    $scope.upload = function(){
      var file = document.getElementById('file').files[0];
      var fileName = file.name;	
      var chunkSize = 1024 * 1024; //1MB
      var numberChunks = Math.ceil(file.size/chunkSize);
      
      $scope.aux = 0;
      $scope.start = 0;
      $scope.stop = chunkSize;
      $scope.canRead = true;

      var blobs = [];

      while($scope.aux < numberChunks){
        var blob = file.slice($scope.start, $scope.stop + 1);
        blobs[$scope.aux] = blob;
        $scope.start = $scope.stop;
        $scope.stop += chunkSize;
        $scope.aux++;
      }
      sendChunks(blobs,fileName);
    }

    var sendChunks = function(blobs, fileName){
      for(i=0; i<blobs.length; i++){
        var formData = new FormData();
        formData.append('chunkNumber', i+1);
        formData.append('chunksExpected', blobs.length);
        formData.append('owner','felipe');
        formData.append('name',fileName);
        formData.append('file',blobs[i]);
        $http.post('/fileuploader/rest/files/upload', formData, {headers: {'Content-Type': undefined }})
        .success(function(result){
        })
        .error(function(result,status){
        });
      }
    };

    $http.get('/fileuploader/rest/TestService/tests')
      .success(function(result){
        $scope.tests = result;
      })
      .error(function(data,status){
        console.log(data);
      });
}]);
