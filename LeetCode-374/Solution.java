// ============================================================================
// 力扣 374. 猜数字大小
// ----------------------------------------------------------------------------
// 题目描述
// 我们正在玩猜数字游戏。游戏规则如下：
// 我从 1 到 n 之间选一个数字，你来猜我选的是哪一个（我选的数字在整个游戏
// 过程中保持不变）。每次你猜错了，我会告诉你我选的数字比你的猜测大还是小。
//
// 你调用一个预先定义好的接口 int guess(int num)，它会返回三种可能的结果：
//   -1：你猜的数字比我选的数字大（num > pick）
//    1：你猜的数字比我选的数字小（num < pick）
//    0：你猜的数字等于我选的数字（num == pick）
//
// 返回我选的那个数字。
//
// 函数签名（力扣已经给出，不用自己读输入，把方法补全提交即可）
//   public class Solution extends GuessGame
//   public int guessNumber(int n)
//   guess(num) 是父类 GuessGame 里已经实现好的接口，直接调用即可。
//   （本地想跑要自己补一个带 guess 的 GuessGame 类，里面存好 pick；
//   力扣提交时不用带，平台会提供。）
//
// 输入 #1
//   n = 10, pick = 6
// 输出 #1
//   6
//
// 输入 #2
//   n = 1, pick = 1
// 输出 #2
//   1
//
// 输入 #3
//   n = 2, pick = 1
// 输出 #3
//   1
//
// 提示
//   1 <= n <= 2^31 - 1
//   1 <= pick <= n
//
// 说明/提示
// 【样例说明】pick 是后台设定的答案，不是方法参数；方法只拿得到 n，其余
//   信息全靠 guess 去问。例 3 里 n = 2、答案是 1：猜 1 直接命中（返回 0），
//   猜 2 会得到 -1（猜大了）。按下面的二分实际走一遍例 1（pick = 6）：
//     left=1, right=10 → mid=5，guess(5)=1（猜小了）→ left=6
//     left=6, right=10 → mid=8，guess(8)=-1（猜大了）→ right=7
//     left=6, right=7  → mid=6，guess(6)=0 → 返回 6，和样例一致。
// 【评测用例规模与约定】
//   n 最大到 2^31 - 1（正好是 int 的最大值 2147483647），
//   力扣难度：简单。answer 一定在 [1, n] 里，二分每次砍掉一半，
//   最多问 O(log n) 次 guess。
//
// 思路（二分查找之"精确找值"：left / right 两个下标 + while）
//   1 到 n 是天然有序的，猜小了答案一定在右边、猜大了答案一定在左边，正好
//   是二分。闭区间 [left, right] 表示"答案可能还在的范围"：
//     ① 初始化 left = 1、right = n；
//     ② 循环条件写 while (left <= right)——区间空了就说明没找到；
//     ③ mid 取中点：mid = left + (right - left) / 2；
//     ④ 问一次 r = guess(mid)：
//          r == 0  → mid 就是答案，直接 return mid；
//          r == -1 → 猜大了，答案比 mid 小，right = mid - 1；
//          r == 1  → 猜小了，答案比 mid 大，left = mid + 1；
//     ⑤ 三个分支把 mid 排除掉时都能 ±1，因为 mid 已经被问过、确认不是答案。
//
//   边界和坑：
//     - n 可以到 2^31 - 1，mid 千万别写 (left + right) / 2：left + right
//       会溢出成负数导致死循环。要写 left + (right - left) / 2；当然用
//       long 存 left / right 再强转也安全；
//     - -1 和 1 的含义最容易记反：-1 是"你猜的数比答案大"，所以答案是更小
//       的一侧，right = mid - 1；别写成 left = mid + 1；
//     - 用 while (left <= right) 必须配 right = mid - 1 / left = mid + 1，
//       不能写成 right = mid，否则区间不缩小会死循环；
//     - 同样因为这题是"精确找值"，mid 确认不是答案后可以放心排除，这一点
//       和 278（找第一个错误版本、right = mid）不同；
//     - n = 1 时第一轮 mid 就是 1，guess(1) = 0 直接返回，覆盖例 2；
//     - 时间 O(log n)，额外空间 O(1)。
// ============================================================================

public class Solution extends GuessGame {
    public int guessNumber(int n) {
        // 初始化左右边界 left = 1、right = n
        int left = 1;
        int right = n;
        // 循环条件写 while (left <= right) 左闭右闭区间，区间空了就说明没找到
        while(left <= right){
            // mid取中间值
            int mid = left + (right - left) / 2;
            // 问一次 r = guess(mid)
            int r = guess(mid);
            // r == 0  → mid 就是答案，直接 return mid
            if(r == 0){
                return mid;
            }else if(r == -1){
                // r == -1 → 猜大了，答案比 mid 小，right = mid - 1（题干决定了，返回-1的话必定是小于mid的，右边界无需多算一遍）
                right = mid - 1;
            }else{
                // r == 1  → 猜小了，答案比 mid 大，left = mid + 1（题干决定了，返回1的话必定是大于mid的，左边界无需多算一遍）
                left = mid + 1;
            }
        }
        // 循环完了还没找到，返回 -1
        return -1;
    }
}