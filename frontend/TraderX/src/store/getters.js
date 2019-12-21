const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews,
  token: state => state.user.token,
  userInfo: state => state.user.userInfo,
  notifications: state => state.user.notifications,
  userSearchResult: state => state.search.userSearchResult,
  articleSearchResult: state => state.search.articleSearchResult,
  articleWriteResult: state => state.search.articleWriteResult,
  userArticle: state => state.search.userArticle,
  myArticles: state => state.search.myArticles,
  allEvents: state => state.search.allEvents,
  equipmentQueryResult: state => state.equipment.equipmentQueryResult,
  currencyResult: state => state.equipment.currencyResult,
  cryptoCurrencyResult: state => state.equipment.cryptoCurrencyResult,
  stockResult: state => state.equipment.stockResult,
  allPortfolios: state => state.equipment.allPortfolios,
  allEquipments: state => state.equipment.allEquipments,
  transaction: state => state.equipment.transaction,
  
  

  // TODO these are deprecated but we can keep useful ones
  roles: state => state.user.roles,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  introduction: state => state.user.introduction,

  permission_routes: state => state.permission.routes,
  errorLogs: state => state.errorLog.logs
}
export default getters
