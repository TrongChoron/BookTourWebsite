const { createSlice } = require("@reduxjs/toolkit");
const authSlice = createSlice({
  name: "auth",
  initialState: {
    user: undefined,
    accessToken: null,
  },
  reducers: {
    authLogin: (state, action) => ({
      ...state,
      // ...action.payload,
    }),
    authRegister: (state, action) => ({
      ...state,
      ...action.payload,
    }),
    authUpdateUser: (state, action) => ({
      ...state,
      user: action.payload.user,
      accessToken: action.payload.accessToken,
    }),
    authFetchMe: (state, action) => ({
      ...state,
    }),
    authRefreshToken: (state, action) => ({}),
    authLogOut: (state, action) => ({}),
    authUpdateAvt: (state, action) => ({
      ...state,
    }),
  },
});
export const {
  authLogin,
  authRegister,
  authUpdateUser,
  authFetchMe,
  authRefreshToken,
  authLogOut,
  authUpdateAvt,
} = authSlice.actions;
export default authSlice.reducer;
