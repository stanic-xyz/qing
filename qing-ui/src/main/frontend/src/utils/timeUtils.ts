const getWeekDate = () => {
  const now = new Date();
  const day = now.getDay();
  const weeks = [
    "星期日",
    "星期一",
    "星期二",
    "星期三",
    "星期四",
    "星期五",
    "星期六",
  ];

  return weeks[day];
};

export default {
  getWeekDate,
};
