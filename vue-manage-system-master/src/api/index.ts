import request from '../utils/request';

export const verify = (data) => {
    return request({
        url: 'http://localhost:8090/user/verify',
        method: 'post',
        data
    });
}

export const uploadMedia = (data) => {
    return request({
        url: 'http://localhost:8090/media/upload',
        method: 'post',
        data
    });
}

export const addFoodOrder = (data) => {
    return request({
        url: 'http://localhost:8090/food-order/add',
        method: 'post',
        data
    });
};

export const updateFoodOrder = (data) => {
    return request({
        url: 'http://localhost:8090/food-order/update',
        method: 'put',
        data
    });
};

export const deleteFoodOrder = (id) => {
    return request({
        url: 'http://localhost:8090/food-order/'+id,
        method: 'delete'
    });
};

export const getFoodOrderList = (params) => {
    return request({
        url: 'http://localhost:8090/food-order/list',
        method: 'get',
        params
    });
};

export const addCompartmentOrder = (data) => {
    return request({
        url: 'http://localhost:8090/compartment-order/add',
        method: 'post',
        data
    });
};

export const updateCompartmentOrder = (data) => {
    return request({
        url: 'http://localhost:8090/compartment-order/update',
        method: 'put',
        data
    });
};

export const deleteCompartmentOrder = (id) => {
    return request({
        url: 'http://localhost:8090/compartment-order/'+id,
        method: 'delete'
    });
};

export const getCompartmentOrderList = (params) => {
    return request({
        url: 'http://localhost:8090/compartment-order/list',
        method: 'get',
        params
    });
};

export const getCompartmentTypeById = (id) => {
    return request({
        url: 'http://localhost:8090/room-type/'+id,
        method: 'get'
    });
};

export const addCompartment = (data) => {
    return request({
        url: 'http://localhost:8090/compartment/add',
        method: 'post',
        data
    });
};

export const updateCompartment = (data) => {
    return request({
        url: 'http://localhost:8090/compartment/update',
        method: 'put',
        data
    });
};

export const deleteCompartment = (id) => {
    return request({
        url: 'http://localhost:8090/compartment/'+id,
        method: 'delete'
    });
};

export const getCompartmentList = (params) => {
    return request({
        url: 'http://localhost:8090/compartment/list',
        method: 'get',
        params
    });
};

export const addFood = (data) => {
    return request({
        url: 'http://localhost:8090/food/add',
        method: 'post',
        data
    });
};

export const updateFood = (data) => {
    return request({
        url: 'http://localhost:8090/foode/update',
        method: 'put',
        data
    });
};

export const deleteFood = (id) => {
    return request({
        url: 'http://localhost:8090/food/'+id,
        method: 'delete'
    });
};

export const getFoodList = (params) => {
    return request({
        url: 'http://localhost:8090/food/list',
        method: 'get',
        params
    });
};

export const addFoodType = (data) => {
    return request({
        url: 'http://localhost:8090/food-type/add',
        method: 'post',
        data
    });
};

export const updateFoodType = (data) => {
    return request({
        url: 'http://localhost:8090/food-type/update',
        method: 'put',
        data
    });
};

export const deleteFoodType = (id) => {
    return request({
        url: 'http://localhost:8090/food-type/'+id,
        method: 'delete'
    });
};

export const getFoodTypeList = (params) => {
    return request({
        url: 'http://localhost:8090/food-type/list',
        method: 'get',
        params
    });
};

export const getFoodTypeById = (id) => {
    return request({
        url: 'http://localhost:8090/food-type/'+id,
        method: 'get'
    });
};

export const addRoomType = (data) => {
    return request({
        url: 'http://localhost:8090/room-type/add',
        method: 'post',
        data
    });
};

export const updateRoomType = (data) => {
    return request({
        url: 'http://localhost:8090/room-type/update',
        method: 'put',
        data
    });
};

export const deleteRoomType = (id) => {
    return request({
        url: 'http://localhost:8090/room-type/'+id,
        method: 'delete'
    });
};

export const getRoomTypeList = (params) => {
    return request({
        url: 'http://localhost:8090/room-type/list',
        method: 'get',
        params
    });
};

export const addSongType = (data) => {
    return request({
        url: 'http://localhost:8090/song-type/add',
        method: 'post',
        data
    });
};

export const updateSongType = (data) => {
    return request({
        url: 'http://localhost:8090/song-type/update',
        method: 'put',
        data
    });
};

export const deleteSongType = (id) => {
    return request({
        url: 'http://localhost:8090/song-type/'+id,
        method: 'delete'
    });
};

export const getSongTypeList = (params) => {
    return request({
        url: 'http://localhost:8090/song-type/list',
        method: 'get',
        params
    });
};


export const addSong = (data) => {
    return request({
        url: 'http://localhost:8090/song/add',
        method: 'post',
        data
    });
};

export const updateSong = (data) => {
    return request({
        url: 'http://localhost:8090/song/update',
        method: 'put',
        data
    });
};

export const deleteSong = (id) => {
    return request({
        url: 'http://localhost:8090/song/'+id,
        method: 'delete'
    });
};

export const getSongTypeById = (id) => {
    return request({
        url: 'http://localhost:8090/song-type/'+id,
        method: 'get'
    });
};

export const getSongList = (params) => {
    return request({
        url: 'http://localhost:8090/song/list',
        method: 'get',
        params
    });
};

export const loginAdmin = (data) => {
    return request({
        url: 'http://localhost:8090/user/login',
        method: 'post',
        data
    });
};

export const getProfile = () => {
    return request({
        url: 'http://localhost:8090/user/get',
        method: 'get'
    });
};

export const updateUser = (data) => {
    return request({
        url: 'http://localhost:8090/user/update',
        method: 'put',
        data
    });
};

export const updateUserProfile = (data) => {
    return request({
        url: 'http://localhost:8090/user',
        method: 'put',
        data
    });
};

export const resetPassword = (data) => {
    return request({
        url: 'http://localhost:8090/password',
        method: 'put',
        data
    });
};

export const getUserList = (params) => {
    return request({
        url: 'http://localhost:8090/user/list',
        method: 'get',
        params
    });
};



export const deleteUser = (id) => {
    return request({
        url: 'http://localhost:8090/user/'+id,
        method: 'delete'
    });
};