app.controller "ListController", [
  "$scope"
  "$http"
  ($scope, $http) ->
    $scope.places = []

    onError = (data, status, headers, config) ->
      console.error(status, headers, config)

    loadList = () ->
      $http
        method: 'GET'
        url: '/places'
      .success (data, status, headers, config) ->
        $scope.places = data
      .error onError

    $scope.addPlace = ->
      if $scope.name and $scope.name != ""
        $http
          method: 'POST'
          url: '/add-place'
          data:
            name: $scope.name
        .success ->
          $scope.name = ""
          loadList()
        .error onError

    $scope.delete = (place) ->
      $http
        method: 'POST'
        url: '/delete-place'
        data:
          name: place.name
      .success ->
        loadList()
      .error onError

    loadList()
]